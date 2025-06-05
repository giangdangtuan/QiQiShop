package com.app85soft.qiqishop.repositories.cart_item;

import com.app85soft.qiqishop.dto.response.cart.CartRes;

import java.util.List;

public interface CartItemRepositoryCustom {
    List<CartRes.CartItemRes> getListCartIteam(List<Integer> ids, int userId);

    void deleteAllByIdsAndUserId(List<Integer> ids, int userId);

}
