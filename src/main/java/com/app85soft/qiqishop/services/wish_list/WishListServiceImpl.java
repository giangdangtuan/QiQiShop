package com.app85soft.qiqishop.services.wish_list;

import com.app85soft.qiqishop.component.Translator;
import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.dto.request.IdsRequest;
import com.app85soft.qiqishop.dto.request.wish_list.AddWishListReq;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.category.CategoryListRes;
import com.app85soft.qiqishop.dto.response.wish_list.WishListRes;
import com.app85soft.qiqishop.entities.category.Category;
import com.app85soft.qiqishop.entities.role.constant.PermissionKey;
import com.app85soft.qiqishop.entities.role.constant.PermissionType;
import com.app85soft.qiqishop.entities.user.User;
import com.app85soft.qiqishop.entities.wish_list.WishList;
import com.app85soft.qiqishop.exceptions.BusinessException;
import com.app85soft.qiqishop.repositories.wish_list.WishListRepository;
import com.app85soft.qiqishop.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl extends BaseService implements WishListService {
    private final WishListRepository wishListRepository;

    @Override
    public BaseResponse<List<WishListRes>> getWishLists() {
        User user = getUser();
        long count = wishListRepository.countWishList(user.getId());
        List<WishListRes> wishList = wishListRepository.getWishLists(user.getId());
        return new BaseResponse<>(wishList, count);
    }

    @Override
    public WishList addWishList(AddWishListReq request) {
        User user = getUser();
        Boolean check = wishListRepository.getProductId(user.getId(), request.getProductId());
        if(check) {
            throw new BusinessException(Translator.toLocale("product_has_in_wishlist"), HttpStatus.BAD_REQUEST);
        }
        WishList newWishList = new WishList();
        newWishList.setUserId(user.getId());
        newWishList.setProductId(request.getProductId());

        return wishListRepository.save(newWishList);
    }

    @Override
    public List<Integer> deleteWishList(IdsRequest request) {
        User user = getUser();
        List<Integer> wishListIds = request.getIds();
        List<Integer> existingIds = wishListRepository.getAllIdToCheckExist(wishListIds);
        List<Integer> nonExistingIds = wishListIds.stream().filter(id -> !existingIds.contains(id)).toList();
        if (!nonExistingIds.isEmpty()) {
            throw new BusinessException(Translator.toLocale("id_not_exist"), HttpStatus.BAD_REQUEST);
        }
        wishListRepository.deleteWishLists(wishListIds);
        return wishListIds;
    }
}
