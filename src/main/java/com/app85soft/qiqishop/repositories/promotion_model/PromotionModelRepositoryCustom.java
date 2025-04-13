package com.app85soft.qiqishop.repositories.promotion_model;

import com.app85soft.qiqishop.dto.response.promotion.PromotionModelRes;
import com.app85soft.qiqishop.entities.promotion.PromotionModel;

import java.util.Collection;
import java.util.List;

public interface PromotionModelRepositoryCustom {

    List<PromotionModelRes> getPromotionModels(int promotionId);

    List<PromotionModel> findConflictingPromotions(int modelId, long startTime, long endTime);

    void softDeletePromotionModels(Collection<PromotionModel> promotionModels);
}
