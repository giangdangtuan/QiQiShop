package com.app85soft.qiqishop.repositories.promotion_model;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.dto.response.promotion.PromotionModelRes;
import com.app85soft.qiqishop.entities.model.Model;
import com.app85soft.qiqishop.entities.promotion.PromotionModel;
import com.app85soft.qiqishop.entities.promotion.QPromotion;
import com.app85soft.qiqishop.entities.promotion.QPromotionModel;
import com.app85soft.qiqishop.repositories.BaseRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class PromotionModelRepositoryImpl extends BaseRepository implements PromotionModelRepositoryCustom {
    private final QPromotionModel qPromotionModel = QPromotionModel.promotionModel;
    private final QPromotion qPromotion = QPromotion.promotion;

    @Override
    public List<PromotionModelRes> getPromotionModels(int promotionId) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qPromotionModel.deleted.eq(false));
        builder.and(qPromotionModel.promotionId.eq(promotionId));

        return query().from(qPromotionModel)
                .where(builder)
                .select(Projections.fields(PromotionModelRes.class,
                        qPromotionModel.id, qPromotionModel.promotionId, qPromotionModel.modelId,
                        qPromotionModel.discountPercentage, qPromotionModel.status))
                .fetch();
    }

    @Override
    public List<PromotionModel> findConflictingPromotions(int modelId, long startTime, long endTime) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qPromotionModel.modelId.eq(modelId));
        builder.and(qPromotionModel.deleted.eq(false));
        builder.and(qPromotion.deleted.eq(false));

        return query().from(qPromotionModel)
                .join(qPromotion).on(qPromotionModel.promotionId.eq(qPromotion.id))
                .where(builder
                        .and(qPromotion.startTime.loe(endTime))
                        .and(qPromotion.endTime.goe(startTime))
                        .and(qPromotion.status.eq(ActiveStatus.ACTIVE))
                )
                .select(qPromotionModel)
                .fetch();
    }

    @Override
    @Transactional
    public void softDeletePromotionModels(Collection<PromotionModel> promotionModels) {
        if (promotionModels == null || promotionModels.isEmpty()) return;

        List<Integer> modelIds = promotionModels.stream()
                .map(PromotionModel::getId)
                .collect(Collectors.toList());

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qPromotionModel.id.in(modelIds));
        builder.and(qPromotionModel.deleted.eq(false));

        query().update(qPromotionModel)
                .set(qPromotionModel.deleted, true)
                .where(builder)
                .execute();
    }
}
