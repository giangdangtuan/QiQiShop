package com.app85soft.qiqishop.repositories.user;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.dto.response.user.UserDetailRes;
import com.app85soft.qiqishop.dto.response.user.UserListRes;
import com.app85soft.qiqishop.entities.user.User;

import java.util.List;

public interface UserRepositoryCustom {
    User loginByPhone(String email);

    User getUserByPhone(String phone);

    boolean existsByCode (String code);

    long countUser(ActiveStatus status, String searchKeyword);

    List<UserListRes> getUsers(ActiveStatus status, String searchKeyword, int page);

    UserDetailRes getProfileUser(int accountId);

    List<Integer> getAllIdToCheckExist(List<Integer> userIds);

    void deleteUsers(List<Integer> userIds);

    User getUserToUpdate(int userId);
}
