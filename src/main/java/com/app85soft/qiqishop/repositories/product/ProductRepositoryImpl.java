package com.app85soft.qiqishop.repositories.product;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.dto.response.model.ModelRes;
import com.app85soft.qiqishop.dto.response.product.ProductRes;
import com.app85soft.qiqishop.entities.model.QModel;
import com.app85soft.qiqishop.entities.product.Product;
import com.app85soft.qiqishop.entities.product.QProduct;
import com.app85soft.qiqishop.repositories.BaseRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.app85soft.qiqishop.util.Constants.PAGE_SIZE;

public class ProductRepositoryImpl extends BaseRepository implements ProductRepositoryCustom {
    private final QProduct qProduct = QProduct.product;
    private final QModel qModel = QModel.model;

    @Override
    public boolean existsByCode(String code) {
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(qProduct.code.eq(code));
        builder.and(qProduct.deleted.eq(false));

        return query().from(qProduct)
                .where(builder)
                .select(qProduct.id)
                .fetchFirst() != null;
    }

    @Override
    public long countProduct(ActiveStatus status, String searchKeyword, Integer categoryId) {
        BooleanBuilder builder = new BooleanBuilder();
        if (status != null) {
            builder.and(qProduct.status.eq(status));
        }
        builder.and(qProduct.deleted.eq(false));
        if (searchKeyword != null) {
            builder.andAnyOf(
                    qProduct.name.contains(searchKeyword));
        }
        if(categoryId != null) {
            builder.and(qProduct.categoryId.eq(categoryId));
        }
        Long count = query().from(qProduct)
                .where(builder)
                .select(qProduct.id.count())
                .fetchOne();
        return count == null ? 0 : count;
    }

    @Override
    public List<ProductRes> getProduct(ActiveStatus status, String searchKeyword, Integer categoryId, int page) {
        BooleanBuilder builder = new BooleanBuilder();
        if (status != null) {
            builder.and(qProduct.status.eq(status));
        }
        builder.and(qProduct.deleted.eq(false));
        if (searchKeyword != null) {
            builder.and(qProduct.name.contains(searchKeyword));
        }
        if (categoryId != null) {
            builder.and(qProduct.categoryId.eq(categoryId));
        }

        List<ProductRes> products = query().from(qProduct)
                .where(builder)
                .orderBy(qProduct.id.desc())
                .offset(page * PAGE_SIZE)
                .limit(PAGE_SIZE)
                .select(Projections.fields(ProductRes.class,
                        qProduct.id,
                        qProduct.code,
                        qProduct.name,
                        qProduct.categoryId,
                        qProduct.coverImage,
                        qProduct.description,
                        qProduct.status
                ))
                .fetch();

        if (products.isEmpty()) {
            return Collections.emptyList();
        }

        List<Integer> productIds = products.stream()
                .map(ProductRes::getId)
                .collect(Collectors.toList());

        List<ModelRes> models = query().from(qModel)
                .where(qModel.productId.in(productIds)
                        .and(qModel.deleted.eq(false)))
                .select(Projections.fields(ModelRes.class,
                        qModel.code,
                        qModel.productId,
                        qModel.name,
                        qModel.coverImage,
                        qModel.hasDiscount,
                        qModel.discountPercentage,
                        qModel.price,
                        qModel.stock,
                        qModel.soldCount
                ))
                .fetch();

        Map<Integer, List<ModelRes>> modelMap = models.stream()
                .collect(Collectors.groupingBy(ModelRes::getProductId));

        products.forEach(product -> product.setModels(modelMap.getOrDefault(product.getId(), new ArrayList<>())));

        return products;
    }


    @Override
    public ProductRes getProductDetail(int productId) {
        ProductRes product = query().from(qProduct)
                .where(qProduct.id.eq(productId).and(qProduct.deleted.eq(false)))
                .select(Projections.fields(ProductRes.class,
                        qProduct.id,
                        qProduct.code,
                        qProduct.name,
                        qProduct.categoryId,
                        qProduct.coverImage,
                        qProduct.description,
                        qProduct.status
                ))
                .fetchOne();

        if (product == null) {
            return null;
        }

        List<ModelRes> models = query().from(qModel)
                .where(qModel.productId.eq(productId)
                        .and(qModel.deleted.eq(false)))
                .select(Projections.fields(ModelRes.class,
                        qModel.code,
                        qModel.productId,
                        qModel.name,
                        qModel.coverImage,
                        qModel.hasDiscount,
                        qModel.discountPercentage,
                        qModel.price,
                        qModel.stock,
                        qModel.soldCount
                ))
                .fetch();

        product.setModels(models);
        return product;
    }


    @Override
    public Product getProductToUpdate(int id) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qProduct.id.eq(id));
        builder.and(qProduct.deleted.eq(false));
        return query().from(qProduct)
                .where(builder)
                .select(qProduct)
                .fetchOne();
    }

    @Override
    public List<Integer> getAllIdToCheckExist(List<Integer> productIds) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qProduct.id.in(productIds));
        builder.and(qProduct.deleted.eq(false));

        return query().from(qProduct)
                .where(builder)
                .select(qProduct.id)
                .fetch();
    }

    @Override
    @Transactional
    public void deleteProducts(List<Integer> productIds) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qProduct.id.in(productIds));
        builder.and(qProduct.deleted.eq(false));

        query().update(qProduct)
                .set(qProduct.deleted, true)
                .where(builder)
                .execute();

        BooleanBuilder modelBuilder = new BooleanBuilder();
        modelBuilder.and(qModel.productId.in(productIds));
        modelBuilder.and(qModel.deleted.eq(false));

        query().update(qModel)
                .set(qModel.deleted, true)
                .where(modelBuilder)
                .execute();
    }
}
