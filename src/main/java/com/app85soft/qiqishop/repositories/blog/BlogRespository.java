package com.app85soft.qiqishop.repositories.blog;

import com.app85soft.qiqishop.entities.blog.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRespository extends JpaRepository<Blog, Integer>, BlogRespositoryCustom {
}
