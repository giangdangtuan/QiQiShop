package com.app85soft.qiqishop.repositories.category;

import java.util.List;

import com.app85soft.qiqishop.entities.upload_file.QUploadFile;
import org.springframework.stereotype.Repository;

import com.app85soft.qiqishop.dto.response.category.CategoryListRes;
import com.app85soft.qiqishop.entities.category.Category;
import com.app85soft.qiqishop.entities.category.QCategory;
import com.app85soft.qiqishop.repositories.BaseRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Repository
public class CategoryRepositoryImpl extends BaseRepository implements CategoryRepositoryCustom {
    private final QCategory qCategory = QCategory.category;
    private final QUploadFile qUploadFile = QUploadFile.uploadFile;

    @Override
    public long countCategory() {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qCategory.deleted.eq(false));
        Long count = query().from(qCategory)
                .where(builder)
                .select(qCategory.id.count())
                .fetchOne();
        return count == null ? 0 : count;
    }

    @Override
    public boolean existsByName(String name, Integer id) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qCategory.name.eq(name));
        builder.and(qCategory.id.ne(id));
        builder.and(qCategory.deleted.eq(false));

        long count = query().from(qCategory)
                .where(builder)
                .fetchCount();

        return count > 0;
    }

    @Override
    public List<CategoryListRes> getCategories() {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qCategory.deleted.eq(false));

        return query().from(qCategory)
                .leftJoin(qUploadFile).on(qUploadFile.id.eq(qCategory.coverImage)
                        .and(qUploadFile.deleted.eq(false)))
                .where(builder)
                .select(Projections.fields(CategoryListRes.class,
                        qCategory.id,
                        qCategory.name,
                        qCategory.coverImage,
                        qUploadFile.originUrl.as("imageUrl"),
                        qCategory.status
                ))
                .fetch();
    }

    @Override
    public CategoryListRes getCategoryDetail(int id) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qCategory.id.eq(id));
        builder.and(qCategory.deleted.eq(false));

        CategoryListRes category = query().from(qCategory)
                .leftJoin(qUploadFile).on(qUploadFile.id.eq(qCategory.coverImage)
                        .and(qUploadFile.deleted.eq(false)))
                .where(builder)
                .select(Projections.fields(CategoryListRes.class,
                        qCategory.id,
                        qCategory.name,
                        qCategory.coverImage,
                        qUploadFile.originUrl.as("imageUrl"),
                        qCategory.status
                ))
                .fetchOne();

        return category;
    }

    @Override
    public Category getCategoryToUpdate(int categoryId) {

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qCategory.id.eq(categoryId));
        builder.and(qCategory.deleted.eq(false));
        return query().from(qCategory)
                .where(builder)
                .select(qCategory)
                .fetchOne();
    }

    @Override
    @Transactional
    public void deleteCategories(List<Integer> categoryIds) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qCategory.id.in(categoryIds));
        builder.and(qCategory.deleted.eq(false));

        query().update(qCategory)
                .set(qCategory.deleted, true)
                .where(builder)
                .execute();
    }

    @Override
    public List<Integer> getAllIdToCheckExist(List<Integer> categoryIds) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qCategory.id.in(categoryIds));
        builder.and(qCategory.deleted.eq(false));

        return query().from(qCategory)
                .where(builder)
                .select(qCategory.id)
                .fetch();
    }

}
