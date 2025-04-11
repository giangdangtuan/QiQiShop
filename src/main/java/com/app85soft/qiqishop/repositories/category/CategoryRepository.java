package com.app85soft.qiqishop.repositories.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app85soft.qiqishop.entities.category.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>, CategoryRepositoryCustom {
   
}
