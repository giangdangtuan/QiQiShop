package com.app85soft.qiqishop.controller;

import com.app85soft.qiqishop.dto.request.IdsRequest;
import com.app85soft.qiqishop.dto.request.wish_list.AddWishListReq;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.wish_list.WishListRes;
import com.app85soft.qiqishop.services.wish_list.WishListService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
@Slf4j
public class WishListController {
    private final WishListService wishListService;

    @Operation(summary = "Add new wish-list")
    @PostMapping("v1/wish-list/add")
    public ResponseEntity<BaseResponse<?>> addWishList(@RequestBody @Valid AddWishListReq req) {
        return ResponseEntity.ok(new BaseResponse<>(wishListService.addWishList(req)));
    }

    @Operation(summary = "Get list wish-list.")
    @GetMapping("v1/wish-list/list")
    public ResponseEntity<BaseResponse<List<WishListRes>>> getWishLists() {
        return ResponseEntity.ok(wishListService.getWishLists());
    }

    @Operation(summary = "Delete wish-list.")
    @PostMapping("v1/wish-list/delete")
    public ResponseEntity<BaseResponse<List<Integer>>> deleteWishLists(@RequestBody @Valid IdsRequest request) {
        return ResponseEntity.ok(new BaseResponse<>(wishListService.deleteWishList(request)));
    }
}
