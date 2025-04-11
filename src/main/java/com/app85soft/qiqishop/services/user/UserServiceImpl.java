package com.app85soft.qiqishop.services.user;

import com.app85soft.qiqishop.component.Translator;
import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.dto.constant.RoleType;
import com.app85soft.qiqishop.dto.constant.VerifyStatus;
import com.app85soft.qiqishop.dto.request.ChangePasswordReq;
import com.app85soft.qiqishop.dto.request.IdsRequest;
import com.app85soft.qiqishop.dto.request.auth.ForgotPasswordReq;
import com.app85soft.qiqishop.dto.request.auth.UserLoginReq;
import com.app85soft.qiqishop.dto.request.user.*;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.role.RoleDetail;
import com.app85soft.qiqishop.dto.response.user.UserDetailRes;
import com.app85soft.qiqishop.dto.response.user.UserListRes;
import com.app85soft.qiqishop.entities.otp.Otp;
import com.app85soft.qiqishop.entities.role.Role;
import com.app85soft.qiqishop.entities.role.constant.PermissionKey;
import com.app85soft.qiqishop.entities.role.constant.PermissionType;
import com.app85soft.qiqishop.entities.role.permisstion.Permission;
import com.app85soft.qiqishop.entities.role.role_permission.RolePermission;
import com.app85soft.qiqishop.entities.user.User;
import com.app85soft.qiqishop.exceptions.BusinessException;
import com.app85soft.qiqishop.repositories.otp.OtpRepository;
import com.app85soft.qiqishop.repositories.permission.PermissionRepository;
import com.app85soft.qiqishop.repositories.role.RolePermissionRepository;
import com.app85soft.qiqishop.repositories.role.RoleRepository;
import com.app85soft.qiqishop.repositories.user.UserRepository;
import com.app85soft.qiqishop.security.JwtTokenProvider;
import com.app85soft.qiqishop.services.BaseService;
import com.app85soft.qiqishop.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends BaseService implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final OtpRepository otpRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    @Value("${app.jwtAdminExpirationInMs}")
    private int jwtExpirationInMs;

    private final int OTP_EXPIRY_IN_MINUTES = 5;

    @Override
    public UserDetailRes login(UserLoginReq request) {
        User user = userRepository.loginByPhone(request.getPhone());
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(Translator.toLocale("login_fail"), HttpStatus.UNAUTHORIZED);
        }
        if (user.getStatus() != ActiveStatus.ACTIVE) {
            throw new BusinessException(Translator.toLocale("account_not_activated"));
        }
        UserDetailRes userLoginRes = getUserRes(user);
        userLoginRes
                .setAccessToken(jwtTokenProvider.generateTokenRs256(String.valueOf(user.getId()), jwtExpirationInMs));
        return userLoginRes;
    }

    @Transactional
    @Override
    public UserDetailRes register(RegisterUser request) {
        Otp sessionAuth = otpRepository.findById(request.getCodeId()).orElse(null);
        if (sessionAuth == null || sessionAuth.getStatus() == VerifyStatus.VERIFIED) {
            throw new BusinessException(Translator.toLocale("register_fail"));
        }
        try {
            if (sessionAuth.getOtp().equals(request.getCode()) && Util.getDuration(new Date(),
                    sessionAuth.getCreatedAt(), TimeUnit.MINUTES) >= OTP_EXPIRY_IN_MINUTES) {
                sessionAuth.setStatus(VerifyStatus.EXPIRED);
                throw new BusinessException(Translator.toLocale("otp_expired"));
            }
            if (sessionAuth.getAttemptCount() <= 0) {
                sessionAuth.setStatus(VerifyStatus.FAILED);
                throw new BusinessException(Translator.toLocale("otp_over"));
            }
            sessionAuth.setAttemptCount(sessionAuth.getAttemptCount() - 1);
            if (!sessionAuth.getPhone().equals(request.getPhone())) {
                sessionAuth.setStatus(VerifyStatus.FAILED);
                throw new BusinessException(Translator.toLocale("register_fail"));
            }
            if (!sessionAuth.getOtp().equals(request.getCode())) {
                throw new BusinessException(Translator.toLocale("otp_wrong"));
            }
            sessionAuth.setStatus(VerifyStatus.VERIFIED);
        } finally {
            otpRepository.save(sessionAuth);
        }

        String code = generateCode(8);

        Role role = new Role();
        role.setObjectId(1);
        role.setStatus(ActiveStatus.ACTIVE);
        role.setName("User");
        role.setType(RoleType.USER);
        roleRepository.save(role);

        List<Permission> permissions = permissionRepository.getPermissions(RoleType.USER);
        List<RolePermission> rolePermissions = new ArrayList<>();
        for (Permission permission : permissions) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(role.getId());
            rolePermission.setPermissionId(permission.getId());
            rolePermission.setIsView(permission.getIsView());
            rolePermission.setIsApproval(permission.getIsApproval());
            rolePermission.setIsWrite(permission.getIsWrite());
            rolePermission.setIsDecision(permission.getIsDecision());
            rolePermissions.add(rolePermission);
        }
        rolePermissionRepository.saveAll(rolePermissions);

        User user = new User();
        user.setCode(code);
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatus(ActiveStatus.ACTIVE);
        user.setRoleId(role.getId());
        user.setRole(role);
        userRepository.save(user);

        UserDetailRes userLoginRes = getUserRes(user);
        userLoginRes
                .setAccessToken(jwtTokenProvider.generateTokenRs256(String.valueOf(user.getId()), jwtExpirationInMs));
        return userLoginRes;

    }

    @Override
    public BaseResponse<List<UserListRes>> getUsers(ActiveStatus status, String searchKeyword, int page) {
        User user = getUser(PermissionKey.READ, PermissionType.ACCOUNT);

        long count = userRepository.countUser(status, searchKeyword, user.getRole());
        List<UserListRes> users = userRepository.getUsers(status, searchKeyword, page, user.getRole());
        return new BaseResponse<>(users, count, page);
    }

    @Override
    public BaseResponse<UserDetailRes> getDetailMember(int accountId) {
        User user = getUser(PermissionKey.READ, PermissionType.ACCOUNT);

        UserDetailRes userRes = userRepository.getProfileUser(accountId, user.getRole());
        if (userRes == null) {
            throw new BusinessException(Translator.toLocale("id_not_exist"), HttpStatus.NOT_FOUND);
        }
        return new BaseResponse<>(userRes);
    }

    @Override
    public User addUserMember(AddUserBaseReq request) {
        User user = getUser(PermissionKey.CREATE, PermissionType.ACCOUNT);
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new BusinessException(Translator.toLocale("phone_already_exists"), HttpStatus.BAD_REQUEST);
        }
        User newUser = new User();
        newUser.setCode(generateCode(8));
        newUser.setName(request.getName());
        newUser.setPhone(request.getPhone());
        newUser.setEmail(request.getEmail());
        newUser.setStatus(ActiveStatus.ACTIVE);
        newUser.setRoleId(request.getRoleId());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        return userRepository.save(newUser);
    }

    @Override
    public UserDetailRes updateMember(UpdateUserReq request) {
        User user = getUser(PermissionKey.CREATE, PermissionType.ACCOUNT);
        User currentUser = userRepository.getUserToUpdate(request.getUserId(), user.getRole());
        if (currentUser == null) {
            throw new BusinessException(Translator.toLocale("user_id_not_exist"));
        }
        if (request.getPhone() != null && !request.getPhone().isEmpty()) {
            currentUser.setPhone(request.getPhone());
        }
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            currentUser.setEmail(request.getEmail());
        }
        if (request.getName() != null && !request.getName().isEmpty()) {
            currentUser.setName(request.getName());
        }
        if (request.getBirthday() != null) {
            currentUser.setBirthday(request.getBirthday());
        }
        if (request.getStatus() != null) {
            currentUser.setStatus(request.getStatus());
        }
        if (request.getGender() != null) {
            currentUser.setGender(request.getGender());
        }
        if (request.getRoleId() != null) {
            currentUser.setRoleId(request.getRoleId());
        }
        userRepository.save(currentUser);
        return getUserRes(currentUser);

    }

    @Override
    public List<Integer> deleteMembers(IdsRequest request) {
        User user = getUser(PermissionKey.DECISION, PermissionType.ACCOUNT);
        List<Integer> userIds = request.getIds();
        if (userIds.contains(user.getId())) {
            throw new BusinessException(Translator.toLocale("can_not_delete_myself"), HttpStatus.BAD_REQUEST);
        }
        List<Integer> existingIds = userRepository.getAllIdToCheckExist(userIds, user.getRole());
        List<Integer> nonExistingIds = userIds.stream().filter(id -> !existingIds.contains(id)).toList();
        if (!nonExistingIds.isEmpty()) {
            throw new BusinessException(Translator.toLocale("id_not_exist"), HttpStatus.BAD_REQUEST);
        }
        userRepository.deleteUsers(userIds);
        return userIds;
    }

    @Override
    public void changePasswordMember(ChangePasswordUserReq request) {
        User user = getUser(PermissionKey.CREATE, PermissionType.ACCOUNT);
        User currentUser = userRepository.getUserToUpdate(request.getUserId(), user.getRole());
        if (currentUser == null) {
            throw new BusinessException(Translator.toLocale("user_id_not_exist"));
        }
        currentUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(currentUser);
    }

    @Override
    public UserDetailRes getMyProfile() {
        User user = getUser();

        return getUserRes(user);
    }

    @Override
    public String forgotPassword(ForgotPasswordReq request) {
        Otp sessionAuth = otpRepository.findById(request.getCodeId()).orElse(null);
        if (sessionAuth == null || sessionAuth.getStatus() == VerifyStatus.VERIFIED) {
            throw new BusinessException(Translator.toLocale("password_unchanged"));
        }
        try {
            if (sessionAuth.getOtp().equals(request.getCode()) && Util.getDuration(new Date(),
                    sessionAuth.getCreatedAt(), TimeUnit.MINUTES) >= OTP_EXPIRY_IN_MINUTES) {
                sessionAuth.setStatus(VerifyStatus.EXPIRED);
                throw new BusinessException(Translator.toLocale("otp_expired"));
            }
            if (sessionAuth.getAttemptCount() <= 0) {
                sessionAuth.setStatus(VerifyStatus.FAILED);
                throw new BusinessException(Translator.toLocale("otp_over"));
            }
            sessionAuth.setAttemptCount(sessionAuth.getAttemptCount() - 1);
            if (!sessionAuth.getOtp().equals(request.getCode())) {
                throw new BusinessException(Translator.toLocale("otp_wrong"));
            }
            sessionAuth.setStatus(VerifyStatus.VERIFIED);
        } finally {
            otpRepository.save(sessionAuth);
        }
        User user = userRepository.getUserByPhone(sessionAuth.getPhone());
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return Translator.toLocale("password_changed");
    }

    @Override
    public void changeMyPassword(ChangePasswordReq request) {
        User user = getUser();
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new BusinessException(Translator.toLocale("password_wrong"));
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public UserDetailRes editMyProfile(EditMyProfileReq request) {
        User user = getUser(PermissionKey.CREATE, PermissionType.ACCOUNT);
        if (request.getName() != null && !request.getName().isEmpty()) {
            user.setName(request.getName());
        }
        if (request.getBirthday() != null) {
            user.setBirthday(request.getBirthday());
        }
        if (request.getGender() != null) {
            user.setGender(request.getGender());
        }
        userRepository.save(user);
        return getUserRes(user);
    }

    private UserDetailRes getUserRes(User user) {
        RoleDetail roleDetail;
        if (user.getRole() == null) {
            roleDetail = roleRepository.getRoleById(user.getRoleId());
        } else {
            roleDetail = new RoleDetail();
            roleDetail.setRoleId(user.getRole().getId());
            roleDetail.setRoleType(user.getRole().getType());
            roleDetail.setObjectId(user.getRole().getObjectId());
            roleDetail.setRoleName(user.getRole().getName());
        }
        return UserDetailRes.builder()
                .id(user.getId())
                .code(user.getCode())
                .name(user.getName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .role(roleDetail)
                .birthday(user.getBirthday())
                .gender(user.getGender())
                .permissions(roleRepository.getPermissions(user.getRoleId(), user.getRole()))
                .build();
    }

    private String generateCode(int count) {
        String code;
        boolean exists;

        do {
            code = Util.randomString(count);
            exists = userRepository.existsByCode(code);
        } while (exists);
        return code;
    }
}
