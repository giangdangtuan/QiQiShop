package com.app85soft.qiqishop.repositories.category;

import org.springframework.stereotype.Repository;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.entities.role.Role;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class CategoryRepositorylmpl implements CategoryRepositoryCustom {

    @Override
    public long countCategory(ActiveStatus status, String searchKeyword, Role role) {
        
        throw new UnsupportedOperationException("Unimplemented method 'countCategory'");
    }

}
