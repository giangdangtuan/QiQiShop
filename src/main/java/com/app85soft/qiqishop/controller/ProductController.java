package com.app85soft.qiqishop.controller;

import com.app85soft.qiqishop.annotations.NoRequireAuth;
import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.dto.request.IdsRequest;
import com.app85soft.qiqishop.dto.request.product.AddProductReq;
import com.app85soft.qiqishop.dto.request.product.UpdateProductReq;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.product.ProductDetailRes;
import com.app85soft.qiqishop.dto.response.product.ProductRes;
import com.app85soft.qiqishop.services.product.ProductService;
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
public class ProductController {

    private final ProductService productService;

    @NoRequireAuth
    @Operation(summary = "Get list product.")
    @GetMapping("v1/product/list")
    public ResponseEntity<BaseResponse<List<ProductRes>>> getProducts(@RequestParam int page,
                                                                      @RequestParam(required = false) ActiveStatus status,
                                                                      @Parameter(description = "[name]")
                                                                      @RequestParam(required = false) String searchKeyword,
                                                                      @Parameter(description = "[category]")
                                                                      @RequestParam(required = false) Integer catgoryId) {
        return ResponseEntity.ok(productService.getProducts(status, searchKeyword, catgoryId, page));
    }


    @Operation(summary = "Add product")
    @PostMapping("v1/product/add")
    public ResponseEntity<BaseResponse<?>> addProduct(@RequestBody @Valid AddProductReq req) {
        return ResponseEntity.ok(new BaseResponse<>(productService.addProduct(req)));
    }

    @Operation(summary = "Update product")
    @PostMapping("v1/product/update")
    public ResponseEntity<BaseResponse<?>> updateProduct(@RequestBody @Valid UpdateProductReq req) {
        return ResponseEntity.ok(new BaseResponse<>(productService.updateProduct(req)));
    }

    @Operation(summary = "Delete products.")
    @PostMapping("v1/product/delete")
    public ResponseEntity<BaseResponse<List<Integer>>> deleteProducts(@RequestBody @Valid IdsRequest request) {
        return ResponseEntity.ok(new BaseResponse<>(productService.deleteProducts(request)));
    }

    @NoRequireAuth
    @Operation(summary = "Get product detail")
    @GetMapping("v1/product/detail/{id}")
    public ResponseEntity<BaseResponse<ProductDetailRes>> getProductDetail(@PathVariable("id") int productId) {
        return ResponseEntity.ok(productService.getProduct(productId));
    }

}
