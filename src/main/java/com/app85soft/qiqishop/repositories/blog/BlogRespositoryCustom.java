package com.app85soft.qiqishop.repositories.blog;

import com.app85soft.qiqishop.dto.constant.BlogStatus;
import com.app85soft.qiqishop.dto.response.blog.BlogRes;
import com.app85soft.qiqishop.entities.blog.Blog;

import java.util.List;

public interface BlogRespositoryCustom {
    long countBlog(BlogStatus status, String searchKeyword);

    List<BlogRes> getBlogs(BlogStatus status, String searchKeyword, int page);

    BlogRes getBlogDetail(int blogId);

    Blog getBlogToUpdate(int blogId);

    void deleteBlogs(List<Integer> blogIds);

    List<Integer> getAllIdToCheckExist(List<Integer> blogIds);
}
