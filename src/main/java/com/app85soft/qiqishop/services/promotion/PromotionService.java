package com.app85soft.qiqishop.services.promotion;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.dto.request.IdsRequest;
import com.app85soft.qiqishop.dto.request.product.AddProductReq;
import com.app85soft.qiqishop.dto.request.product.UpdateProductReq;
import com.app85soft.qiqishop.dto.request.promotion.AddPromotionReq;
import com.app85soft.qiqishop.dto.request.promotion.UpdatePromotionReq;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.product.ProductRes;
import com.app85soft.qiqishop.dto.response.promotion.PromotionRes;

import java.util.List;

public interface PromotionService {
    PromotionRes addPromotion(AddPromotionReq promotionReq);
    PromotionRes updatePromotion(UpdatePromotionReq promotionReq);
    List<Integer> deletePromotions(IdsRequest req);
    BaseResponse<List<PromotionRes>> getPromotions(ActiveStatus status, String name, Long startTime, Long endTime, int page);
    BaseResponse<PromotionRes> getPromotion(int promotionId);
}
