package com.app85soft.qiqishop.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app85soft.qiqishop.dto.request.category.AddCategoryReq;
import com.app85soft.qiqishop.dto.request.category.UpdateCategoryReq;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.category.CategoryListRes;
import com.app85soft.qiqishop.entities.category.Category;
import com.app85soft.qiqishop.services.category.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(summary = "Add new category")
    @PostMapping("v1/category/add")
    public ResponseEntity<BaseResponse<Category>> addCategory(@RequestBody @Valid AddCategoryReq request) {
        return ResponseEntity.ok(new BaseResponse<>(categoryService.addCategory(request)));
    }

    @Operation(summary = "Update category")
    @PostMapping("v1/category/update")
    public ResponseEntity<BaseResponse<List<CategoryListRes> >> updateCategory(@RequestBody @Valid UpdateCategoryReq request) {
        return ResponseEntity.ok(new BaseResponse<>(categoryService.updateCategory(request)));
    }

    
}
