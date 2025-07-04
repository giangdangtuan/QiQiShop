package com.app85soft.qiqishop.controller;

import java.util.List;

import com.app85soft.qiqishop.annotations.NoRequireAuth;
import com.app85soft.qiqishop.dto.request.IdsRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app85soft.qiqishop.dto.request.category.AddCategoryReq;
import com.app85soft.qiqishop.dto.request.category.UpdateCategoryReq;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.category.CategoryListRes;
import com.app85soft.qiqishop.services.category.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(summary = "Add new category")
    @PostMapping("v1/category/add")
    public ResponseEntity<BaseResponse<?>> addCategory(@RequestBody @Valid AddCategoryReq req) {
        return ResponseEntity.ok(new BaseResponse<>(categoryService.addCategory(req)));
    }

    @Operation(summary = "Update category")
    @PostMapping("v1/category/update")
    public ResponseEntity<BaseResponse<CategoryListRes>> updateCategory(@RequestBody @Valid UpdateCategoryReq request) {
        return ResponseEntity.ok(new BaseResponse<>(categoryService.updateCategory(request)));
    }

    @NoRequireAuth
    @Operation(summary = "Get list category.")
    @GetMapping("v1/category/list")
    public ResponseEntity<BaseResponse<List<CategoryListRes>>> getCategories() {
        return ResponseEntity.ok(categoryService.getCategories());
    }

    @Operation(summary = "Get category detail.")
    @GetMapping("v1/category/detail/{id}")
    public ResponseEntity<BaseResponse<CategoryListRes>> getCategoryDetail(@PathVariable("id") int catId) {
        return ResponseEntity.ok(new BaseResponse<>(categoryService.getCategoryDetail(catId)));
    }

    @Operation(summary = "Delete categories.")
    @PostMapping("v1/category/delete")
    public ResponseEntity<BaseResponse<List<Integer>>> deleteCategories(@RequestBody @Valid IdsRequest request) {
        return ResponseEntity.ok(new BaseResponse<>(categoryService.deleteCategory(request)));
    }
}
