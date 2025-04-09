package com.app85soft.qiqishop.repositories.category;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.entities.role.Role;

public interface CategoryRepositoryCustom {
    long countCategory(ActiveStatus status, String searchKeyword, Role role);
    
}
