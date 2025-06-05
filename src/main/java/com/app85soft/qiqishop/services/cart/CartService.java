package com.app85soft.qiqishop.services.cart;

import com.app85soft.qiqishop.dto.request.IdsRequest;
import com.app85soft.qiqishop.dto.request.cart.AddToCartReq;
import com.app85soft.qiqishop.dto.request.cart.UpdateCartReq;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.cart.AddToCartRes;
import com.app85soft.qiqishop.dto.response.cart.CartRes;

import java.util.List;

public interface CartService {
    AddToCartRes addToCart(AddToCartReq addToCartReq);

    BaseResponse<CartRes> updateCart(UpdateCartReq cartReq);

    BaseResponse<CartRes> deleteCartItem(IdsRequest req);

    BaseResponse<CartRes> getCart();

}
