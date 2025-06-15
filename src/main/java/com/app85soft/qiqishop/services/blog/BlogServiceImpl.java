package com.app85soft.qiqishop.services.blog;

import com.app85soft.qiqishop.component.Translator;
import com.app85soft.qiqishop.dto.constant.BlogStatus;
import com.app85soft.qiqishop.dto.request.IdsRequest;
import com.app85soft.qiqishop.dto.request.blog.AddBlogReq;
import com.app85soft.qiqishop.dto.request.blog.UpdateBlogReq;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.blog.BlogRes;
import com.app85soft.qiqishop.entities.blog.Blog;
import com.app85soft.qiqishop.entities.role.constant.PermissionKey;
import com.app85soft.qiqishop.entities.role.constant.PermissionType;
import com.app85soft.qiqishop.entities.user.User;
import com.app85soft.qiqishop.exceptions.BusinessException;
import com.app85soft.qiqishop.repositories.blog.BlogRespository;
import com.app85soft.qiqishop.services.BaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlogServiceImpl extends BaseService implements BlogService {
    private final BlogRespository blogRespository;

    @Override
    public BaseResponse<List<BlogRes>> getBlogs(BlogStatus status, String searchKeyword, int page) {
        long count = blogRespository.countBlog(status, searchKeyword);
        List<BlogRes> blogs = blogRespository.getBlogs(status, searchKeyword, page);
        return new BaseResponse<>(blogs, count);
    }

    @Override
    public BlogRes addBlog(AddBlogReq req) {
        User user = getUser(PermissionKey.CREATE, PermissionType.POST);

        Blog newBlog = new Blog();
        newBlog.setTitle(req.getTitle());
        newBlog.setAuthorId(user.getId());
        newBlog.setThumbnailId(req.getThumbnailId());
        newBlog.setContent(req.getContent());
        newBlog.setDescription(req.getDescription());
        newBlog.setStatus(req.getStatus());
        blogRespository.save(newBlog);

        return blogRespository.getBlogDetail(newBlog.getId());
    }

    @Override
    public BlogRes updateBlog(UpdateBlogReq req) {
        User user = getUser(PermissionKey.CREATE, PermissionType.POST);
        Blog currentBlog = blogRespository.getBlogToUpdate(req.getId());
        if (currentBlog == null) {
            throw new BusinessException(Translator.toLocale("category_id_not_exist"));
        }
        if (req.getTitle() != null && !req.getTitle().isEmpty()) {
            currentBlog.setTitle(req.getTitle());
        }
        if (req.getThumbnailId() != null && req.getThumbnailId() != 0) {
            currentBlog.setThumbnailId(req.getThumbnailId());
        }
        if (req.getContent() != null && !req.getContent().isEmpty()) {
            currentBlog.setContent(req.getContent());
        }
        if (req.getDescription() != null && !req.getDescription().isEmpty()) {
            currentBlog.setDescription(req.getDescription());
        }
        if (req.getStatus() != null) {
            currentBlog.setStatus(req.getStatus());
        }
        blogRespository.save(currentBlog);

        return blogRespository.getBlogDetail(currentBlog.getId());
    }

    @Override
    public List<Integer> deleteBlog(IdsRequest req) {
        User user = getUser(PermissionKey.DECISION, PermissionType.POST);
        List<Integer> blogIds = req.getIds();
        List<Integer> existingIds = blogRespository.getAllIdToCheckExist(blogIds);
        List<Integer> nonExistingIds = blogIds.stream().filter(id -> !existingIds.contains(id)).toList();
        if (!nonExistingIds.isEmpty()) {
            throw new BusinessException(Translator.toLocale("id_not_exist"), HttpStatus.BAD_REQUEST);
        }
        blogRespository.deleteBlogs(blogIds);
        return blogIds;
    }

    @Override
    public BlogRes getBlogDetail(int blogId) {
        BlogRes blog = blogRespository.getBlogDetail(blogId);
        if (blog == null) {
            throw new BusinessException(Translator.toLocale("id_not_exist"), HttpStatus.BAD_REQUEST);
        }

        return blog;
    }
}
