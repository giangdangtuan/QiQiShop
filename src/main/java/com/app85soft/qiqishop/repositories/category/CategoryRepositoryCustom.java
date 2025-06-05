package com.app85soft.qiqishop.repositories.category;

import java.util.List;

import com.app85soft.qiqishop.dto.response.category.CategoryListRes;
import com.app85soft.qiqishop.entities.category.Category;

public interface CategoryRepositoryCustom {
    long countCategory();

    boolean existsByName(String name, Integer id);

    List<CategoryListRes> getCategories();

    CategoryListRes getCategoryDetail(int id);
    
    Category getCategoryToUpdate(int categoryId);

    void deleteCategories(List<Integer> categoryIds);

    List<Integer> getAllIdToCheckExist(List<Integer> categoryIds);

}
