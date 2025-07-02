package com.app85soft.qiqishop.repositories.wish_list;

import com.app85soft.qiqishop.dto.response.category.CategoryListRes;
import com.app85soft.qiqishop.dto.response.wish_list.WishListRes;
import com.app85soft.qiqishop.entities.category.QCategory;
import com.app85soft.qiqishop.entities.product.QProduct;
import com.app85soft.qiqishop.entities.upload_file.QUploadFile;
import com.app85soft.qiqishop.entities.user.QUser;
import com.app85soft.qiqishop.entities.wish_list.QWishList;
import com.app85soft.qiqishop.repositories.BaseRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Repository
public class WishListRepositoryImpl extends BaseRepository implements WishListRepositoryCustom {
    private final QWishList qWishList = QWishList.wishList;
    private final QProduct qProduct = QProduct.product;
    private final QCategory qCategory = QCategory.category;
    private final QUploadFile qUploadFile = QUploadFile.uploadFile;
    private final QUser user = QUser.user;

    @Override
    public long countWishList(int userId) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qWishList.userId.eq(userId));
        builder.and(qWishList.deleted.eq(false));
        Long count = query().from(qWishList)
                .leftJoin(qProduct).on(qWishList.productId.eq(qProduct.id)
                        .and(qProduct.deleted.eq(false)))
                .where(builder)
                .select(qWishList.id.count())
                .fetchOne();
        return count == null ? 0 : count;
    }

    @Override
    public Boolean getProductId(int userId, int productId) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qWishList.productId.eq(productId));
        builder.and(qWishList.userId.eq(userId));
        builder.and(qWishList.deleted.eq(false));

        Integer result = query().from(qWishList)
                .where(builder)
                .select(qWishList.id)
                .fetchFirst();

        return result != null;
    }

    @Override
    public List<WishListRes> getWishLists(int userId) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qWishList.userId.eq(userId));
        builder.and(qWishList.deleted.eq(false));

        return query().from(qWishList)
                .leftJoin(qProduct).on(qWishList.productId.eq(qProduct.id)
                        .and(qProduct.deleted.eq(false)))
                .leftJoin(qUploadFile).on(qUploadFile.id.eq(qProduct.coverImage)
                        .and(qUploadFile.deleted.eq(false)))
                .leftJoin(qCategory).on(qCategory.id.eq(qProduct.categoryId)
                        .and(qCategory.deleted.eq(false)))
                .where(builder)
                .select(Projections.fields(WishListRes.class,
                        qWishList.id,
                        qWishList.userId,
                        qWishList.productId,
                        qProduct.name.as("productName"),
                        qUploadFile.originUrl,
                        qUploadFile.thumbUrl,
                        qCategory.name.as("categoryName"),
                        qWishList.createdAt
                ))
                .fetch();
    }

    @Transactional
    @Override
    public void deleteWishLists(List<Integer> wishListIds) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qWishList.id.in(wishListIds));
        builder.and(qWishList.deleted.eq(false));

        query().update(qWishList)
                .set(qWishList.deleted, true)
                .where(builder)
                .execute();
    }

    @Override
    public List<Integer> getAllIdToCheckExist(List<Integer> wishListIds) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qWishList.id.in(wishListIds));
        builder.and(qWishList.deleted.eq(false));

        return query().from(qWishList)
                .where(builder)
                .select(qWishList.id)
                .fetch();
    }
}
