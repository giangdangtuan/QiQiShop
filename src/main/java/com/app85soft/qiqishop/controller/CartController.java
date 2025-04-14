package com.app85soft.qiqishop.controller;

import com.app85soft.qiqishop.dto.request.IdsRequest;
import com.app85soft.qiqishop.dto.request.cart.AddToCartReq;
import com.app85soft.qiqishop.dto.request.cart.UpdateCartReq;
import com.app85soft.qiqishop.dto.request.product.UpdateProductReq;
import com.app85soft.qiqishop.dto.request.promotion.AddPromotionReq;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.cart.CartRes;
import com.app85soft.qiqishop.dto.response.promotion.PromotionRes;
import com.app85soft.qiqishop.services.cart.CartService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @Operation(summary = "Add to cart")
    @PostMapping("v1/cart/add")
    public ResponseEntity<BaseResponse<?>> addToCart(@RequestBody @Valid AddToCartReq req) {
        return ResponseEntity.ok(new BaseResponse<>(cartService.addToCart(req)));
    }

    @Operation(summary = "Update cart")
    @PostMapping("v1/cart/update")
    public ResponseEntity<BaseResponse<?>> updateCart(@RequestBody @Valid List<UpdateCartReq> req) {
        return ResponseEntity.ok(new BaseResponse<>(cartService.updateCart(req)));
    }

    @Operation(summary = "Delete cart.")
    @PostMapping("v1/cart/delete")
    public ResponseEntity<BaseResponse<CartRes>> deleteCartItems(@RequestBody @Valid IdsRequest request) {
        return ResponseEntity.ok(cartService.deleteCartItem(request));
    }

    @Operation(summary = "Get cart detail")
    @GetMapping("v1/cart/detail")
    public ResponseEntity<BaseResponse<CartRes>> getCartDetail() {
        return ResponseEntity.ok(cartService.getCart());
    }
}
