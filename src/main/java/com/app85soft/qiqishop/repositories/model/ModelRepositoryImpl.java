package com.app85soft.qiqishop.repositories.model;

import com.app85soft.qiqishop.dto.response.model.ModelRes;
import com.app85soft.qiqishop.entities.model.Model;
import com.app85soft.qiqishop.entities.model.QModel;
import com.app85soft.qiqishop.entities.role.QRole;
import com.app85soft.qiqishop.repositories.BaseRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ModelRepositoryImpl extends BaseRepository implements ModelRepositoryCustom {
    private final QModel qModel = QModel.model;
    private final QRole qRole = QRole.role;

    @Override
    public boolean existsByCode(String code) {
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(qModel.code.eq(code));
        builder.and(qModel.deleted.eq(false));

        return query().from(qModel)
                .where(builder)
                .select(qModel.id)
                .fetchFirst() != null;
    }

    @Override
    public List<ModelRes> getModels(int productId) {

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qModel.deleted.eq(false));
        builder.and(qModel.productId.eq(productId));

        return query().from(qModel)
                .where(builder)
                .select(Projections.fields(ModelRes.class,
                        qModel.code, qModel.name, qModel.productId,
                        qModel.coverImage, qModel.hasDiscount,
                        qModel.discountPercentage, qModel.price,
                        qModel.stock, qModel.soldCount))
                .fetch();
    }

    @Override
    @Transactional
    public void softDeleteModels(Collection<Model> models) {
        if (models == null || models.isEmpty()) return;

        List<Integer> modelIds = models.stream()
                .map(Model::getId)
                .collect(Collectors.toList());

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qModel.id.in(modelIds));
        builder.and(qModel.deleted.eq(false));

        query().update(qModel)
                .set(qModel.deleted, true)
                .where(builder)
                .execute();
    }
}
