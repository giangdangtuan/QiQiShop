package com.app85soft.qiqishop.repositories.rating;

import com.app85soft.qiqishop.dto.response.rating.RatingRes;
import com.app85soft.qiqishop.entities.model.QModel;
import com.app85soft.qiqishop.entities.product.QProduct;
import com.app85soft.qiqishop.entities.rating.QRating;
import com.app85soft.qiqishop.entities.user.QUser;
import com.app85soft.qiqishop.repositories.BaseRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.app85soft.qiqishop.util.Constants.PAGE_SIZE;

@Slf4j
@Repository
public class RatingRepositoryImpl extends BaseRepository implements RatingRepositoryCustom {
    private final QRating qRating = QRating.rating;
    private final QProduct qProduct = QProduct.product;
    private final QModel qModel = QModel.model;
    private final QUser qUser = QUser.user;

    @Override
    public long countRating(int productId) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qProduct.id.eq(productId));
        builder.and(qRating.deleted.eq(false));

        Long count = query().from(qRating)
                .leftJoin(qModel).on(qRating.modelId.eq(qModel.id))
                .leftJoin(qProduct).on(qModel.productId.eq(qProduct.id))
                .where(builder)
                .select(qRating.id.count())
                .fetchOne();
        return count == null ? 0 : count;
    }

    @Override
    public List<RatingRes> getRatings(int productId, int page) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qProduct.id.eq(productId));
        builder.and(qRating.deleted.eq(false));

        List<RatingRes> ratings = query().from(qRating)
                .leftJoin(qModel).on(qRating.modelId.eq(qModel.id))
                .leftJoin(qProduct).on(qModel.productId.eq(qProduct.id)
                        .and(qProduct.deleted.eq(false)))
                .leftJoin(qUser).on(qRating.userId.eq(qUser.id))
                .where(builder)
                .orderBy(qRating.id.desc())
                .offset((long) page * PAGE_SIZE)
                .limit(PAGE_SIZE)
                .select(Projections.fields(RatingRes.class,
                        qRating.id,
                        qRating.userId,
                        qRating.orderId,
                        qRating.modelId,
                        qRating.ratingImage,
                        qRating.content,
                        qRating.ratingStar,
                        qRating.createdAt,
                        qModel.name.as("modelName"),
                        qUser.name.as("userName")
                ))
                .fetch();
        return ratings;
    }

    @Override
    public Double getAverageRating(int productId) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qProduct.id.eq(productId));
        builder.and(qRating.deleted.eq(false));
        builder.and(qProduct.deleted.eq(false));

        Double avg = query().from(qRating)
                .leftJoin(qModel).on(qRating.modelId.eq(qModel.id))
                .leftJoin(qProduct).on(qModel.productId.eq(qProduct.id))
                .where(builder)
                .select(qRating.ratingStar.avg())
                .fetchOne();
        return avg != null ? avg : 5.0;
    }
}
