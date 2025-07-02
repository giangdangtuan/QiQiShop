package com.app85soft.qiqishop.services.wish_list;

import com.app85soft.qiqishop.dto.request.IdsRequest;
import com.app85soft.qiqishop.dto.request.wish_list.AddWishListReq;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.wish_list.WishListRes;
import com.app85soft.qiqishop.entities.wish_list.WishList;

import java.util.List;

public interface WishListService {
    BaseResponse<List<WishListRes>> getWishLists();

    WishList addWishList(AddWishListReq request);

    List<Integer> deleteWishList(IdsRequest request);
}
