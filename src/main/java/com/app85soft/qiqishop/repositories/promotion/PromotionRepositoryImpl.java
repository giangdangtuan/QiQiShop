package com.app85soft.qiqishop.repositories.promotion;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.dto.response.promotion.PromotionModelRes;
import com.app85soft.qiqishop.dto.response.promotion.PromotionRes;
import com.app85soft.qiqishop.entities.promotion.Promotion;
import com.app85soft.qiqishop.entities.promotion.QPromotion;
import com.app85soft.qiqishop.entities.promotion.QPromotionModel;
import com.app85soft.qiqishop.repositories.BaseRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.app85soft.qiqishop.util.Constants.PAGE_SIZE;

@Slf4j
@Repository
public class PromotionRepositoryImpl extends BaseRepository implements PromotionRepositoryCustom {
    private final QPromotion qPromotion = QPromotion.promotion;
    private final QPromotionModel qPromotionModel = QPromotionModel.promotionModel;

    @Override
    public long countPromotion(ActiveStatus status, String searchKeyword, Long startTime, Long endTime) {
        BooleanBuilder builder = new BooleanBuilder();
        if (status != null) {
            builder.and(qPromotion.status.eq(status));
        }
        builder.and(qPromotion.deleted.eq(false));
        if (searchKeyword != null) {
            builder.andAnyOf(
                    qPromotion.name.contains(searchKeyword));
        }
        if(startTime != null) {
            builder.and(qPromotion.startTime.goe(startTime));
        }
        if(endTime != null) {
            builder.and(qPromotion.endTime.loe(endTime));
        }
        Long count = query().from(qPromotion)
                .where(builder)
                .select(qPromotion.id.count())
                .fetchOne();
        return count == null ? 0 : count;
    }

    @Override
    public Promotion getPromotionToUpdate(int id) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qPromotion.id.eq(id));
        builder.and(qPromotion.deleted.eq(false));
        return query().from(qPromotion)
                .where(builder)
                .select(qPromotion)
                .fetchOne();
    }

    @Override
    public List<PromotionRes> getPromotions(ActiveStatus status, String searchKeyword, Long startTime, Long endTime, int page) {
        BooleanBuilder builder = new BooleanBuilder();
        if (status != null) {
            builder.and(qPromotion.status.eq(status));
        }
        builder.and(qPromotion.deleted.eq(false));
        if (searchKeyword != null) {
            builder.and(qPromotion.name.contains(searchKeyword));
        }
        if(startTime != null) {
            builder.and(qPromotion.startTime.goe(startTime));
        }
        if(endTime != null) {
            builder.and(qPromotion.endTime.loe(endTime));
        }

        List<PromotionRes> promotionts = query().from(qPromotion)
                .where(builder)
                .orderBy(qPromotion.id.desc())
                .offset(page * PAGE_SIZE)
                .limit(PAGE_SIZE)
                .select(Projections.fields(PromotionRes.class,
                        qPromotion.id,
                        qPromotion.name,
                        qPromotion.startTime,
                        qPromotion.endTime,
                        qPromotion.status
                ))
                .fetch();

        if (promotionts.isEmpty()) {
            return Collections.emptyList();
        }

        List<Integer> promotionIds = promotionts.stream()
                .map(PromotionRes::getId)
                .collect(Collectors.toList());

        List<PromotionModelRes> promotionModels = query().from(qPromotionModel)
                .where(qPromotionModel.promotionId.in(promotionIds)
                        .and(qPromotionModel.deleted.eq(false)))
                .select(Projections.fields(PromotionModelRes.class,
                        qPromotionModel.id,
                        qPromotionModel.promotionId,
                        qPromotionModel.modelId,
                        qPromotionModel.discountPercentage,
                        qPromotionModel.status
                ))
                .fetch();

        Map<Integer, List<PromotionModelRes>> promotionModelMap = promotionModels.stream()
                .collect(Collectors.groupingBy(PromotionModelRes::getPromotionId));

        promotionts.forEach(promotion -> promotion.setPromotionModels(promotionModelMap.getOrDefault(promotion.getId(), new ArrayList<>())));

        return promotionts;
    }

    @Override
    public PromotionRes getPromotionDetail(int promotionId) {
        PromotionRes promotion = query().from(qPromotion)
                .where(qPromotion.id.eq(promotionId).and(qPromotion.deleted.eq(false)))
                .select(Projections.fields(PromotionRes.class,
                        qPromotion.id,
                        qPromotion.name,
                        qPromotion.startTime,
                        qPromotion.endTime,
                        qPromotion.status
                ))
                .fetchOne();

        if (promotion == null) {
            return null;
        }

        List<PromotionModelRes> promotionModels = query().from(qPromotionModel)
                .where(qPromotionModel.promotionId.eq(promotionId)
                        .and(qPromotionModel.deleted.eq(false)))
                .select(Projections.fields(PromotionModelRes.class,
                        qPromotionModel.id,
                        qPromotionModel.promotionId,
                        qPromotionModel.modelId,
                        qPromotionModel.discountPercentage,
                        qPromotionModel.status
                ))
                .fetch();

        promotion.setPromotionModels(promotionModels);
        return promotion;
    }

    @Override
    public List<Integer> getAllIdToCheckExist(List<Integer> promotionIds) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qPromotion.id.in(promotionIds));
        builder.and(qPromotion.deleted.eq(false));

        return query().from(qPromotion)
                .where(builder)
                .select(qPromotion.id)
                .fetch();
    }

    @Override
    @Transactional
    public void deletePromotions(List<Integer> promotionIds) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qPromotion.id.in(promotionIds));
        builder.and(qPromotion.deleted.eq(false));

        query().update(qPromotion)
                .set(qPromotion.deleted, true)
                .where(builder)
                .execute();

        BooleanBuilder promotionModelBuilder = new BooleanBuilder();
        promotionModelBuilder.and(qPromotionModel.promotionId.in(promotionIds));
        promotionModelBuilder.and(qPromotionModel.deleted.eq(false));

        query().update(qPromotionModel)
                .set(qPromotionModel.deleted, true)
                .where(promotionModelBuilder)
                .execute();
    }
}
