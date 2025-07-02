package com.app85soft.qiqishop.repositories.category;

import com.app85soft.qiqishop.entities.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>, CategoryRepositoryCustom {
    boolean existsByName(String name);
    Category findCategoryById(int id);
}