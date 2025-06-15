package com.app85soft.qiqishop.repositories.blog;

import com.app85soft.qiqishop.dto.constant.BlogStatus;
import com.app85soft.qiqishop.dto.response.blog.BlogRes;
import com.app85soft.qiqishop.entities.blog.Blog;
import com.app85soft.qiqishop.entities.blog.QBlog;
import com.app85soft.qiqishop.entities.upload_file.QUploadFile;
import com.app85soft.qiqishop.entities.user.QUser;
import com.app85soft.qiqishop.repositories.BaseRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class BlogRespositoryImpl extends BaseRepository implements BlogRespositoryCustom {
    private final QBlog qBlog = QBlog.blog;
    private final QUser qUser = QUser.user;
    private final QUploadFile qUploadFile = QUploadFile.uploadFile;

    @Override
    public long countBlog(BlogStatus status, String searchKeyword) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qBlog.deleted.eq(false));

        if (status != null) {
            builder.and(qBlog.status.eq(status));
        }
        if (searchKeyword != null) {
            builder.andAnyOf(
                    qBlog.title.contains(searchKeyword));
        }

        Long count = query().from(qBlog)
                .where(builder)
                .select(qBlog.id.count())
                .fetchOne();
        return count == null ? 0 : count;
    }

    @Override
    public List<BlogRes> getBlogs(BlogStatus status, String searchKeyword, int page) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qBlog.deleted.eq(false));

        if (status != null) {
            builder.and(qBlog.status.eq(status));
        }
        if (searchKeyword != null) {
            builder.andAnyOf(
                    qBlog.title.contains(searchKeyword));
        }

        return query().from(qBlog)
                .leftJoin(qUploadFile).on(qUploadFile.id.eq(qBlog.thumbnailId)
                        .and(qUploadFile.deleted.eq(false)))
                .leftJoin(qUser).on(qUser.id.eq(qBlog.authorId))
                .where(builder)
                .select(Projections.fields(BlogRes.class,
                        qBlog.id,
                        qBlog.title,
                        qBlog.authorId,
                        qUser.name.as("authorName"),
                        qBlog.thumbnailId,
                        qBlog.content,
                        qBlog.description,
                        qUploadFile.originUrl.as("originUrl"),
                        qUploadFile.thumbUrl.as("thumbUrl"),
                        qBlog.status
                ))
                .fetch();
    }

    @Override
    public BlogRes getBlogDetail(int blogId) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qBlog.id.eq(blogId));
        builder.and(qBlog.deleted.eq(false));

        BlogRes blog = query().from(qBlog)
                .leftJoin(qUploadFile).on(qUploadFile.id.eq(qBlog.thumbnailId)
                        .and(qUploadFile.deleted.eq(false)))
                .leftJoin(qUser).on(qUser.id.eq(qBlog.authorId))
                .where(builder)
                .select(Projections.fields(BlogRes.class,
                        qBlog.id,
                        qBlog.title,
                        qBlog.authorId,
                        qUser.name.as("authorName"),
                        qBlog.thumbnailId,
                        qBlog.content,
                        qBlog.description,
                        qUploadFile.originUrl.as("originUrl"),
                        qUploadFile.thumbUrl.as("thumbUrl"),
                        qBlog.status
                ))
                .fetchOne();

        return blog;
    }

    @Override
    public Blog getBlogToUpdate(int blogId) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qBlog.id.eq(blogId));
        builder.and(qBlog.deleted.eq(false));
        return query().from(qBlog)
                .where(builder)
                .select(qBlog)
                .fetchOne();
    }

    @Override
    public void deleteBlogs(List<Integer> blogIds) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qBlog.id.in(blogIds));
        builder.and(qBlog.deleted.eq(false));

        query().update(qBlog)
                .set(qBlog.deleted, true)
                .where(builder)
                .execute();
    }

    @Override
    public List<Integer> getAllIdToCheckExist(List<Integer> blogIds) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qBlog.id.in(blogIds));
        builder.and(qBlog.deleted.eq(false));

        return query().from(qBlog)
                .where(builder)
                .select(qBlog.id)
                .fetch();
    }
}
