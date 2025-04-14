package com.app85soft.qiqishop.repositories.cart;

import com.app85soft.qiqishop.dto.response.cart.CartRes;

import java.util.List;

public interface CartRepositoryCustom {
    CartRes getCartByUserId(int userId);
    List<Integer> getAllCartIteamIdToCheckExist(List<Integer> cartIteamIds);

}
