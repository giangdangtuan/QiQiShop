package com.app85soft.qiqishop.services.category;

import java.util.List;

import org.springframework.stereotype.Service;

import com.app85soft.qiqishop.component.Translator;
import com.app85soft.qiqishop.dto.constant.ActiveStatus;
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

        long count = categoryRepository.countCategory(status, searchKeyword);
        List<CategoryListRes> categories = categoryRepository.getCategories();
        return new BaseResponse<>(categories, count, page);
    }

    @Override
    public Category addCategory(AddCategoryReq request) {
    User user = getUser(PermissionKey.READ, PermissionType.PRODUCT);
       
        Category newCategory = new Category();
        newCategory.setName(request.getName());

        return categoryRepository.save(newCategory);
    }

    

    // @Override
    // public List<Integer> deleteCategory(IdsRequest request) {
    //     User user = getUser(PermissionKey.DECISION, PermissionType.ACCOUNT);
    //     List<Integer> categoryIds = request.getIds();
    //     List<Integer> existingIds = categoryRepository.getAllIdToCheckExist(categoryIds);
    //     List<Integer> nonExistingIds = categoryIds.stream().filter(id -> !existingIds.contains(id)).toList();
    //     if (!nonExistingIds.isEmpty()) {
    //         throw new BusinessException(Translator.toLocale("id_not_exist"), HttpStatus.BAD_REQUEST);
    //     }
    //     categoryRepository.deleteCategories(categoryIds);
    //     return categoryIds;
    // }

    @Override
    public List<CategoryListRes>  updateCategory(UpdateCategoryReq request) {
        User user = getUser(PermissionKey.DECISION, PermissionType.ACCOUNT);
        Category currentCategory = categoryRepository.getCategoryToUpdate(request.getCategoryId());
        
        if (currentCategory == null) {
            throw new BusinessException(Translator.toLocale("category_id_not_exist"));
        }
        if (request.getName() != null && !request.getName().isEmpty()) {
            currentCategory.setName(request.getName());
        }
        categoryRepository.save(currentCategory);
        List<CategoryListRes> categories = categoryRepository.getCategories();
        return categories;
    }

}
