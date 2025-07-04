package com.app85soft.qiqishop.repositories.product;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.dto.response.model.ModelRes;
import com.app85soft.qiqishop.dto.response.model.VariantOptionRes;
import com.app85soft.qiqishop.dto.response.model.VariantValueRes;
import com.app85soft.qiqishop.dto.response.product.ProductDetailRes;
import com.app85soft.qiqishop.dto.response.product.ProductImageRes;
import com.app85soft.qiqishop.dto.response.product.ProductRes;
import com.app85soft.qiqishop.entities.model.QModel;
import com.app85soft.qiqishop.entities.model.QVariantOption;
import com.app85soft.qiqishop.entities.model.QVariantValue;
import com.app85soft.qiqishop.entities.product.Product;
import com.app85soft.qiqishop.entities.product.QProduct;
import com.app85soft.qiqishop.entities.product.QProductImage;
import com.app85soft.qiqishop.entities.promotion.QPromotion;
import com.app85soft.qiqishop.entities.promotion.QPromotionModel;
import com.app85soft.qiqishop.entities.rating.QRating;
import com.app85soft.qiqishop.entities.upload_file.QUploadFile;
import com.app85soft.qiqishop.repositories.BaseRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.app85soft.qiqishop.util.Constants.PAGE_SIZE;

@Slf4j
@Repository
public class ProductRepositoryImpl extends BaseRepository implements ProductRepositoryCustom {
    private final QProduct qProduct = QProduct.product;
    private final QModel qModel = QModel.model;
    private final QPromotion qPromotion = QPromotion.promotion;
    private final QPromotionModel qPromotionModel = QPromotionModel.promotionModel;
    private final QProductImage qProductImage = QProductImage.productImage;
    private final QVariantOption qVariantOption = QVariantOption.variantOption;
    private final QVariantValue qVariantValue = QVariantValue.variantValue;
    private final QUploadFile qUploadFile = QUploadFile.uploadFile;
    private final QRating qRating = QRating.rating;

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
    public boolean existsByName(String name, Integer id) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qProduct.name.eq(name));
        builder.and(qProduct.id.ne(id));
        builder.and(qProduct.deleted.eq(false));

        long count = query().from(qProduct)
                .where(builder)
                .fetchCount();

        return count > 0;
    }

    @Override
    public long countProduct(ActiveStatus status, String searchKeyword, List<Integer> categoryId, BigDecimal startPrice, BigDecimal endPrice) {
        BooleanBuilder builder = new BooleanBuilder();
        if (status != null) {
            builder.and(qProduct.status.eq(status));
        }
        builder.and(qProduct.deleted.eq(false));
        if (searchKeyword != null) {
            builder.andAnyOf(
                    qProduct.name.contains(searchKeyword));
        }
        if (categoryId != null && !categoryId.isEmpty()) {
            builder.and(qProduct.categoryId.in(categoryId));
        }
        if (startPrice != null || endPrice != null) {
            JPQLQuery<Integer> subQuery = query().select(qModel.productId).from(qModel);
            if (startPrice != null) {
                subQuery.where(qModel.price.goe(startPrice));
            }
            if (endPrice != null) {
                subQuery.where(qModel.price.loe(endPrice));
            }
            builder.and(qProduct.id.in(subQuery));
        }

        Long count = query().from(qProduct)
                .where(builder)
                .select(qProduct.id.count())
                .fetchOne();
        return count == null ? 0 : count;
    }

    @Override
    public List<ProductRes> getProduct(ActiveStatus status, String searchKeyword, List<Integer> categoryId,
                                       BigDecimal startPrice, BigDecimal endPrice, int page) {
        BooleanBuilder builder = new BooleanBuilder();
        if (status != null) {
            builder.and(qProduct.status.eq(status));
        }
        builder.and(qProduct.deleted.eq(false));
        if (searchKeyword != null) {
            builder.and(qProduct.name.contains(searchKeyword));
        }
        if (categoryId != null && !categoryId.isEmpty()) {
            builder.and(qProduct.categoryId.in(categoryId));
        }
        if (startPrice != null || endPrice != null) {
            BooleanBuilder priceBuilder = new BooleanBuilder();
            if (startPrice != null) {
                priceBuilder.and(qModel.price.goe(startPrice));
            }
            if (endPrice != null) {
                priceBuilder.and(qModel.price.loe(endPrice));
            }

            JPQLQuery<Integer> subQuery = query()
                    .select(qModel.productId)
                    .from(qModel)
                    .where(priceBuilder);

            builder.and(qProduct.id.in(subQuery));
        }

        List<ProductRes> products = query().from(qProduct)
                .leftJoin(qUploadFile).on(qUploadFile.id.eq(qProduct.coverImage)
                        .and(qUploadFile.deleted.eq(false)))
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
                        qUploadFile.originUrl.as("imageUrl"),
                        qProduct.weight,
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

        Map<Integer, List<ModelRes>> modelMap = new HashMap<>();
        for (int productId : productIds) {
            List<ModelRes> models = getModelsWithPromotion(productId);
            modelMap.put(productId, models);
        }

        Map<Integer, Double> averageRatings = query()
                .select(Projections.tuple(
                        qRating.productId,
                        qRating.ratingStar.avg()
                ))
                .from(qRating)
                .where(qRating.productId.in(productIds)
                        .and(qRating.deleted.eq(false)))
                .groupBy(qRating.productId)
                .fetch()
                .stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(qRating.productId),
                        tuple -> Optional.ofNullable(tuple.get(qRating.ratingStar.avg())).orElse(5.0)
                ));


        products.forEach(product -> {
            product.setModels(modelMap.getOrDefault(product.getId(), new ArrayList<>()));
            product.setAverageRating(averageRatings.getOrDefault(product.getId(), 5.0));
        });

        return products;
    }



    @Override
    public ProductDetailRes getProductDetail(int productId) {
        ProductDetailRes product = query().from(qProduct)
                .leftJoin(qUploadFile).on(qUploadFile.id.eq(qProduct.coverImage)
                        .and(qUploadFile.deleted.eq(false)))
                .where(qProduct.id.eq(productId).and(qProduct.deleted.eq(false)))
                .select(Projections.fields(ProductDetailRes.class,
                        qProduct.id,
                        qProduct.code,
                        qProduct.name,
                        qProduct.categoryId,
                        qProduct.coverImage,
                        qUploadFile.originUrl.as("imageUrl"),
                        qProduct.weight,
                        qProduct.description,
                        qProduct.status
                ))
                .fetchOne();

        if (product == null) {
            return null;
        }

        List<VariantOptionRes> variantOptions = query().from(qVariantOption)
                .where(qVariantOption.productId.eq(productId)
                        .and(qVariantOption.deleted.eq(false)))
                .select(Projections.fields(VariantOptionRes.class,
                        qVariantOption.id,
                        qVariantOption.name
                ))
                .fetch();

        for (VariantOptionRes option : variantOptions) {
            List<VariantValueRes> values = query().from(qVariantValue)
                    .where(qVariantValue.optionTypeId.eq(option.getId())
                            .and(qVariantValue.deleted.eq(false)))
                    .select(Projections.fields(VariantValueRes.class,
                            qVariantValue.id,
                            qVariantValue.value
                    ))
                    .fetch();
            option.setValues(values);
        }

        product.setVariantOptions(variantOptions);

        List<ModelRes> models = getModelsWithPromotion(productId);
        product.setModels(models);

        List<ProductImageRes> productImageRes = query().from(qProductImage)
                .leftJoin(qUploadFile).on(qUploadFile.id.eq(qProductImage.imageId)
                        .and(qUploadFile.deleted.eq(false)))
                .where(qProductImage.productId.eq(productId)
                        .and(qProductImage.deleted.eq(false)))
                .select(Projections.fields(ProductImageRes.class,
                        qProductImage.productId,
                        qProductImage.imageId,
                        qUploadFile.originUrl.as("imageUrl"),
                        qProductImage.sortOrder
                ))
                .fetch();

        product.setProductImages(productImageRes);

        return product;
    }


    @Override
    public List<ProductRes> getProductSale(ActiveStatus status, String searchKeyword, Integer categoryId, int page) {
        BooleanBuilder builder = new BooleanBuilder();
        if (status != null) builder.and(qProduct.status.eq(status));
        builder.and(qProduct.deleted.eq(false));
        if (searchKeyword != null) builder.and(qProduct.name.contains(searchKeyword));
        if (categoryId != null) builder.and(qProduct.categoryId.eq(categoryId));

        List<ProductRes> products = query().from(qProduct)
                .leftJoin(qUploadFile).on(qUploadFile.id.eq(qProduct.coverImage)
                        .and(qUploadFile.deleted.eq(false)))
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
                        qUploadFile.originUrl.as("imageUrl"),
                        qProduct.weight,
                        qProduct.description,
                        qProduct.status
                ))
                .fetch();

        if (products.isEmpty()) return Collections.emptyList();

        List<Integer> productIds = products.stream()
                .map(ProductRes::getId)
                .collect(Collectors.toList());

        long now = System.currentTimeMillis();

        BooleanExpression promotionCondition = qPromotionModel.id.isNotNull()
                .and(qPromotion.startTime.loe(now))
                .and(qPromotion.endTime.goe(now));

        List<ModelRes> models = query().from(qModel)
                .leftJoin(qPromotionModel).on(qPromotionModel.modelId.eq(qModel.id)
                        .and(qPromotionModel.status.eq(ActiveStatus.ACTIVE)))
                .leftJoin(qPromotion).on(qPromotion.id.eq(qPromotionModel.promotionId)
                        .and(qPromotion.status.eq(ActiveStatus.ACTIVE)))
                .where(qModel.productId.in(productIds).and(qModel.deleted.eq(false))
                        .and(qPromotion.startTime.loe(now))
                        .and(qPromotion.endTime.goe(now)))
                .select(Projections.fields(ModelRes.class,
                        qModel.id,
                        qModel.code,
                        qModel.productId,
                        qModel.optionValue1Id,
                        qModel.optionValue2Id,
                        qModel.name,
//                        qModel.coverImage,
                        qModel.price.as("originalPrice"),
                        Expressions.cases()
                                .when(promotionCondition)
                                .then(qModel.price.subtract(
                                        qModel.price.multiply(qPromotionModel.discountPercentage.doubleValue()).divide(100)
                                ))
                                .otherwise(qModel.price).as("finalPrice"),
                        qModel.stock,
                        qModel.soldCount,
                        Projections.fields(ModelRes.PromotionInfo.class,
                                qPromotion.id.as("id"),
                                qPromotion.name.as("name"),
                                qPromotion.startTime.as("startTime"),
                                qPromotion.endTime.as("endTime"),
                                Expressions.cases()
                                        .when(promotionCondition)
                                        .then(qPromotionModel.discountPercentage)
                                        .otherwise(0)
                                        .as("discountPercentage")
                        ).as("promotion")
                ))
                .fetch();

        Map<Integer, List<ModelRes>> modelMap = models.stream()
                .collect(Collectors.groupingBy(ModelRes::getProductId));

        products.forEach(product -> product.setModels(modelMap.getOrDefault(product.getId(), new ArrayList<>())));

        return products;
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
    public List<Integer> getAllIdByCategoryId(List<Integer> categoryId) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qProduct.categoryId.in(categoryId));
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

    private List<ModelRes> getModelsWithPromotion(int productId) {
        long now = System.currentTimeMillis();

        BooleanExpression promotionCondition = qPromotionModel.id.isNotNull()
                .and(qPromotion.startTime.loe(now))
                .and(qPromotion.endTime.goe(now));

        List<ModelRes> models = query().from(qModel)
                .leftJoin(qPromotionModel).on(qPromotionModel.modelId.eq(qModel.id)
                        .and(qPromotionModel.status.eq(ActiveStatus.ACTIVE)))
                .leftJoin(qPromotion).on(qPromotion.id.eq(qPromotionModel.promotionId)
                        .and(qPromotion.status.eq(ActiveStatus.ACTIVE))
                        .and(qPromotion.startTime.loe(now))
                        .and(qPromotion.endTime.goe(now)))
                .where(qModel.productId.eq(productId)
                        .and(qModel.deleted.eq(false)))
                .select(Projections.fields(ModelRes.class,
                        qModel.id,
                        qModel.code,
                        qModel.productId,
                        qModel.optionValue1Id,
                        qModel.optionValue2Id,
                        qModel.name,
//                        qModel.coverImage,
                        qModel.price.as("originalPrice"),
                        Expressions.cases()
                                .when(promotionCondition)
                                .then(qModel.price.subtract(
                                        qModel.price.multiply(qPromotionModel.discountPercentage.doubleValue()).divide(100)
                                ))
                                .otherwise(qModel.price).as("finalPrice"),
                        qModel.stock,
                        qModel.soldCount,
                        Projections.fields(ModelRes.PromotionInfo.class,
                                qPromotion.id.as("id"),
                                qPromotion.name.as("name"),
                                qPromotion.startTime.as("startTime"),
                                qPromotion.endTime.as("endTime"),
                                Expressions.cases()
                                        .when(promotionCondition)
                                        .then(qPromotionModel.discountPercentage)
                                        .otherwise(0)
                                        .as("discountPercentage")
                        ).as("promotion")
                ))
                .fetch();

        Map<String, ModelRes> uniqueModels = new HashMap<>();
        models.forEach(model -> {
            String code = model.getCode();
            ModelRes existing = uniqueModels.get(code);
            if (existing == null || (model.getPromotion() != null)) {
                uniqueModels.put(code, model);
            }
        });

        return new ArrayList<>(uniqueModels.values());
    }

}
