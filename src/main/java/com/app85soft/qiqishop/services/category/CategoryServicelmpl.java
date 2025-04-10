package com.app85soft.qiqishop.services.category;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.app85soft.qiqishop.component.Translator;
import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.dto.request.IdsRequest;
import com.app85soft.qiqishop.dto.request.category.AddCategoryReq;
import com.app85soft.qiqishop.dto.request.category.UpdateCategoryReq;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.category.CategoryListRes;
import com.app85soft.qiqishop.entities.category.Category;
import com.app85soft.qiqishop.entities.role.constant.PermissionKey;
import com.app85soft.qiqishop.entities.role.constant.PermissionType;
import com.app85soft.qiqishop.entities.user.User;
import com.app85soft.qiqishop.exceptions.BusinessException;
import com.app85soft.qiqishop.repositories.category.CategoryRepository;
import com.app85soft.qiqishop.services.BaseService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServicelmpl extends BaseService implements CategoryService {

    private final CategoryRepository categoryRepository;

    public BaseResponse<List<CategoryListRes>> getCategories(ActiveStatus status, String searchKeyword, int page) {
        User user = getUser(PermissionKey.READ, PermissionType.PRODUCT);

        long count = categoryRepository.countCategory(status, searchKeyword, user.getRole());
        List<CategoryListRes> categories = categoryRepository.getCategories(status, searchKeyword, page,
                user.getRole());
        return new BaseResponse<>(categories, count, page);
    }

    @Override
    public Category addCategory(AddCategoryReq request) {
    User user = getUser(PermissionKey.READ, PermissionType.PRODUCT);
        if (categoryRepository.existByName(request.getName())) {
            throw new BusinessException(Translator.toLocale("phone_already_exists"), HttpStatus.BAD_REQUEST);
        }
        Category newCategory = new Category();
        newCategory.setName(request.getName());

        return categoryRepository.save(newCategory);
    }

    @Override
    public BaseResponse<List<CategoryListRes>> updateCategory(UpdateCategoryReq request,ActiveStatus status, String searchKeyword, int page) {
        User user = getUser(PermissionKey.DECISION, PermissionType.ACCOUNT);
        Category currentCategory = categoryRepository.getCategoryToUpdate(request.getCategoryId(), user.getRole());
        
        if (currentCategory == null) {
            throw new BusinessException(Translator.toLocale("category_id_not_exist"));
        }
        if (request.getName() != null && !request.getName().isEmpty()) {
            currentCategory.setName(request.getName());
        }
        categoryRepository.save(currentCategory);
        long count = categoryRepository.countCategory(status, searchKeyword, user.getRole());
        List<CategoryListRes> categories = categoryRepository.getCategories(status, searchKeyword, page,
                user.getRole());
        return new BaseResponse<>(categories, count, page);
    }

    @Override
    public List<Integer> deleteCategory(IdsRequest request) {
        User user = getUser(PermissionKey.DECISION, PermissionType.ACCOUNT);
        
        throw new UnsupportedOperationException("Unimplemented method 'deleteCategory'");
    }

}
