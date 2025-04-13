package com.app85soft.qiqishop.repositories.promotion;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.dto.response.promotion.PromotionRes;
import com.app85soft.qiqishop.entities.promotion.Promotion;

import java.util.List;

public interface PromotionRepositoryCustom {

    long countPromotion(ActiveStatus status, String searchKeyword, Long startTime, Long endTime);

    Promotion getPromotionToUpdate(int id);

    List<PromotionRes> getPromotions(ActiveStatus status, String searchKeyword, Long startTime, Long endTime, int page);

    PromotionRes getPromotionDetail(int promotionId);

    List<Integer> getAllIdToCheckExist(List<Integer> promotionIds);

    void deletePromotions(List<Integer> promotionIds);

}
