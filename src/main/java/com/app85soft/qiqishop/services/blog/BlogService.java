package com.app85soft.qiqishop.services.blog;

import com.app85soft.qiqishop.dto.constant.BlogStatus;
import com.app85soft.qiqishop.dto.request.IdsRequest;
import com.app85soft.qiqishop.dto.request.blog.AddBlogReq;
import com.app85soft.qiqishop.dto.request.blog.UpdateBlogReq;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.blog.BlogRes;

import java.util.List;

public interface BlogService {
    BaseResponse<List<BlogRes>> getBlogs(BlogStatus status, String searchKeyword, int page);

    BlogRes addBlog(AddBlogReq req);

    BlogRes updateBlog(UpdateBlogReq req);

    List<Integer> deleteBlog(IdsRequest req);

    BlogRes getBlogDetail(int blogId);
}
