package com.app85soft.qiqishop.controller;

import com.app85soft.qiqishop.annotations.NoRequireAuth;
import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.dto.constant.BlogStatus;
import com.app85soft.qiqishop.dto.request.IdsRequest;
import com.app85soft.qiqishop.dto.request.blog.AddBlogReq;
import com.app85soft.qiqishop.dto.request.blog.UpdateBlogReq;
import com.app85soft.qiqishop.dto.request.category.AddCategoryReq;
import com.app85soft.qiqishop.dto.request.category.UpdateCategoryReq;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.blog.BlogRes;
import com.app85soft.qiqishop.dto.response.category.CategoryListRes;
import com.app85soft.qiqishop.services.blog.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class BlogController {
    private final BlogService blogService;

    @Operation(summary = "Add new blog")
    @PostMapping("v1/blog/add")
    public ResponseEntity<BaseResponse<?>> addCategory(@RequestBody @Valid AddBlogReq req) {
        return ResponseEntity.ok(new BaseResponse<>(blogService.addBlog(req)));
    }

    @Operation(summary = "Update blog")
    @PostMapping("v1/blog/update")
    public ResponseEntity<BaseResponse<BlogRes>> updateCategory(@RequestBody @Valid UpdateBlogReq req) {
        return ResponseEntity.ok(new BaseResponse<>(blogService.updateBlog(req)));
    }

    @NoRequireAuth
    @Operation(summary = "Get list blog.")
    @GetMapping("v1/blog/list")
    public ResponseEntity<BaseResponse<List<BlogRes>>> getCategories(@RequestParam int page,
                                                                     @RequestParam(required = false) BlogStatus status,
                                                                     @Parameter(description = "[title]")
                                                                     @RequestParam(required = false) String searchKeyword) {
        return ResponseEntity.ok(blogService.getBlogs(status, searchKeyword, page));
    }

    @Operation(summary = "Get blog detail.")
    @GetMapping("v1/blog/detail/{id}")
    public ResponseEntity<BaseResponse<BlogRes>> getCategoryDetail(@PathVariable("id") int blogId) {
        return ResponseEntity.ok(new BaseResponse<>(blogService.getBlogDetail(blogId)));
    }

    @Operation(summary = "Delete blogs.")
    @PostMapping("v1/blog/delete")
    public ResponseEntity<BaseResponse<List<Integer>>> deleteCategories(@RequestBody @Valid IdsRequest req) {
        return ResponseEntity.ok(new BaseResponse<>(blogService.deleteBlog(req)));
    }
}
