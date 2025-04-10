package com.app85soft.qiqishop.repositories.category;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.dto.response.category.CategoryListRes;
import com.app85soft.qiqishop.dto.response.role.RoleDetail;
import com.app85soft.qiqishop.dto.response.user.UserListRes;
import com.app85soft.qiqishop.entities.category.Category;
import com.app85soft.qiqishop.entities.category.QCategory;
import com.app85soft.qiqishop.entities.role.Role;
import com.app85soft.qiqishop.repositories.BaseRepository;
import static com.app85soft.qiqishop.util.Constants.PAGE_SIZE;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class CategoryRepositorylmpl extends BaseRepository implements CategoryRepositoryCustom {
    private final QCategory qCategory = QCategory.category;

    @Override
    public long countCategory(ActiveStatus status, String searchKeyword) {
        BooleanBuilder builder = new BooleanBuilder();

        if (status != null) {
            builder.and(qCategory.status.eq(status));
        }
        builder.and(qCategory.deleted.eq(false));
        if (searchKeyword != null) {
            builder.andAnyOf(
                    qCategory.name.contains(searchKeyword));
        }
        Long count = query().from(qCategory)
                .where(builder)
                .select(qCategory.id.count())
                .fetchOne();
        return count == null ? 0 : count;
    }

    @Override
    public List<CategoryListRes> getCategories() {
        BooleanBuilder builder = new BooleanBuilder();
    
        return query().from(qCategory)
                .where(builder)
                .select(Projections.fields(CategoryListRes.class,
                        qCategory.id, qCategory.name))                     
                .fetch();
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
    public void deleteCategories(List<Integer> categorycategoryIds) {

        throw new UnsupportedOperationException("Unimplemented method 'deleteCategories'");
    }

    @Override
    public List<Integer> getAllIdToCheckExist(List<Integer> categoryIds) {

        throw new UnsupportedOperationException("Unimplemented method 'getAllIdToCheckExist'");
    }

}
