package com.app85soft.qiqishop.services.user;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.dto.request.ChangePasswordReq;
import com.app85soft.qiqishop.dto.request.IdsRequest;
import com.app85soft.qiqishop.dto.request.auth.ForgotPasswordReq;
import com.app85soft.qiqishop.dto.request.auth.UserLoginReq;
import com.app85soft.qiqishop.dto.request.user.*;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.user.UserDetailRes;
import com.app85soft.qiqishop.dto.response.user.UserListRes;
import com.app85soft.qiqishop.entities.user.User;

import java.util.List;

public interface UserService {
    UserDetailRes login(UserLoginReq request);

    UserDetailRes register(RegisterUser request);

    BaseResponse<List<UserListRes>> getUsers(ActiveStatus status, String searchKeyword, int page);

    BaseResponse<UserDetailRes> getDetailMember(int accountId);

    User addUserMember(AddUserBaseReq request);

    UserDetailRes updateMember(UpdateUserReq request);

    List<Integer> deleteMembers(IdsRequest request);

    void changePasswordMember(ChangePasswordUserReq request);

    UserDetailRes getMyProfile();

    void changeMyPassword(ChangePasswordReq request);

    String forgotPassword(ForgotPasswordReq request);

    UserDetailRes editMyProfile(EditMyProfileReq request);

}
