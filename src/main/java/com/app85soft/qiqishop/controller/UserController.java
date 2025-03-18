package com.app85soft.qiqishop.controller;

import com.app85soft.qiqishop.component.Translator;
import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.dto.request.ChangePasswordReq;
import com.app85soft.qiqishop.dto.request.IdsRequest;
import com.app85soft.qiqishop.dto.request.user.AddUserBaseReq;
import com.app85soft.qiqishop.dto.request.user.ChangePasswordUserReq;
import com.app85soft.qiqishop.dto.request.user.EditMyProfileReq;
import com.app85soft.qiqishop.dto.request.user.UpdateUserReq;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.user.UserDetailRes;
import com.app85soft.qiqishop.dto.response.user.UserListRes;
import com.app85soft.qiqishop.entities.user.User;
import com.app85soft.qiqishop.services.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "Get list members")
    @GetMapping("v1/user/members")
    public ResponseEntity<BaseResponse<List<UserListRes>>> getUsers(@RequestParam int page,
                                                                    @RequestParam(required = false) ActiveStatus status,
                                                                    @Parameter(description = "[name, email]")
                                                                    @RequestParam(required = false) String searchKeyword) {
        return ResponseEntity.ok(userService.getUsers(status, searchKeyword, page));
    }

    @Operation(summary = "Get profile account")
    @GetMapping("v1/user/detail/{id}")
    public ResponseEntity<BaseResponse<UserDetailRes>> getDetailMember(@PathVariable("id") int accountId) {
        return ResponseEntity.ok(userService.getDetailMember(accountId));
    }

    @Operation(summary = "Add new account")
    @PostMapping("v1/user/add-member")
    public ResponseEntity<BaseResponse<User>> addUserMember(@RequestBody @Valid AddUserBaseReq request) {
        return ResponseEntity.ok(new BaseResponse<>(userService.addUserMember(request)));
    }

    @Operation(summary = "Update profile for user other")
    @PostMapping("v1/user/update-member")
    public ResponseEntity<BaseResponse<UserDetailRes>> updateMember(@RequestBody @Valid UpdateUserReq request) {
        return ResponseEntity.ok(new BaseResponse<>(userService.updateMember(request)));
    }

    @Operation(summary = "Delete members.")
    @PostMapping("v1/user/delete-members")
    public ResponseEntity<BaseResponse<List<Integer>>> deleteMembers(@RequestBody @Valid IdsRequest request) {
        return ResponseEntity.ok(new BaseResponse<>(userService.deleteMembers(request)));
    }

    @Operation(summary = "Change password for user other")
    @PostMapping("v1/user/change-password-member")
    public ResponseEntity<BaseResponse<?>> changePasswordMember(@RequestBody @Valid ChangePasswordUserReq request) {
        userService.changePasswordMember(request);
        return ResponseEntity.ok(new BaseResponse<>(Translator.toLocale("label_success")));
    }

    @Operation(summary = "Get my profile")
    @GetMapping("v1/user/get-my-profile")
    public ResponseEntity<BaseResponse<UserDetailRes>> getMyProfile() {
        return ResponseEntity.ok(new BaseResponse<>(userService.getMyProfile()));
    }

    @Operation(summary = "Change password of myself")
    @PostMapping("v1/user/change-my-password")
    public ResponseEntity<BaseResponse<?>> changePassword(@RequestBody @Valid ChangePasswordReq request) {
        userService.changeMyPassword(request);
        return ResponseEntity.ok(new BaseResponse<>(Translator.toLocale("label_success")));
    }

    @Operation(summary = "User update my profile")
    @PostMapping("v1/user/edit-my-profile")
    public ResponseEntity<BaseResponse<UserDetailRes>> editMyProfile(@RequestBody @Valid EditMyProfileReq request) {
        return ResponseEntity.ok(new BaseResponse<>(userService.editMyProfile(request)));
    }

}
