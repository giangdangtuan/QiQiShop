package com.app85soft.qiqishop.repositories.category;

import java.util.List;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.dto.response.category.CategoryListRes;
import com.app85soft.qiqishop.entities.category.Category;
import com.app85soft.qiqishop.entities.role.Role;

public interface CategoryRepositoryCustom {
    long countCategory(ActiveStatus status, String searchKeyword, Role role);

    List<CategoryListRes> getCategories(ActiveStatus status, String searchKeyword, int page, Role role);

    Category getCategoryToUpdate(int categoryId, Role role);

    void deleteCategories(List<Integer> categorycategoryIds);

    List<Integer> getAllIdToCheckExist(List<Integer> categoryIds, Role role);

}
