package com.app85soft.qiqishop.services;

import com.app85soft.qiqishop.component.Translator;
import com.app85soft.qiqishop.dto.constant.RoleType;
import com.app85soft.qiqishop.entities.role.constant.PermissionKey;
import com.app85soft.qiqishop.entities.role.constant.PermissionType;
import com.app85soft.qiqishop.entities.user.User;
import com.app85soft.qiqishop.exceptions.BusinessException;
import com.app85soft.qiqishop.repositories.role.RoleRepository;
import com.app85soft.qiqishop.security.SecurityContexts;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@Log4j2
public class BaseService {
    @Autowired
    private RoleRepository roleRepository;

    protected User getUser() {
        try {
            return (User) SecurityContexts.getContext().getData();
        } catch (Exception e) {
            log.error("getUser", e);
        }
        throw new BusinessException(Translator.toLocale("login_required"), HttpStatus.UNAUTHORIZED);
    }

    protected User getUser(PermissionKey key, PermissionType... groups) {
        User user = getUser();
        if (key == null && (groups == null || groups.length == 0)) {
            return user;
        }
        if (roleRepository.existPermission(user.getRoleId(), groups, key, null)) {
            return user;
        }
        throw new BusinessException(Translator.toLocale("invalid_permission"));
    }

//    protected User getUser(RoleType roleType, PermissionKey key, PermissionType... groups) {
//        User user = getUser();
//        if (roleType == null && key == null && (groups == null || groups.length == 0)) {
//            return user;
//        }
//        if (roleRepository.existPermission(user.getRoleId(), groups, key, roleType)) {
//            return user;
//        }
//        throw new BusinessException(Translator.toLocale("invalid_permission"));
//    }

}
