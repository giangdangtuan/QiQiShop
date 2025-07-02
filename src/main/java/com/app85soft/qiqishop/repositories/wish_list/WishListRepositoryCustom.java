package com.app85soft.qiqishop.repositories.wish_list;

import com.app85soft.qiqishop.dto.response.wish_list.WishListRes;

import java.util.List;

public interface WishListRepositoryCustom {
    long countWishList(int userId);

    Boolean getProductId(int userId, int productId);

    List<WishListRes> getWishLists(int userId);

    void deleteWishLists(List<Integer> wishListIds);

    List<Integer> getAllIdToCheckExist(List<Integer> wishListIds);
}
