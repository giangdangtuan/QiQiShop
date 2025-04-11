package com.app85soft.qiqishop.services.category;

import java.util.List;
import java.util.stream.Collectors;

import com.app85soft.qiqishop.dto.request.IdsRequest;
import com.app85soft.qiqishop.repositories.product.ProductRepository;
import org.springframework.http.HttpStatus;
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
    private final ProductRepository productRepository;

    public BaseResponse<List<CategoryListRes>> getCategories() {
        User user = getUser(PermissionKey.READ, PermissionType.PRODUCT);

        long count = categoryRepository.countCategory();
        List<CategoryListRes> categories = categoryRepository.getCategories();
        return new BaseResponse<>(categories, count);
    }

    @Override
    public Category addCategory(AddCategoryReq request) {
    User user = getUser(PermissionKey.READ, PermissionType.PRODUCT);
        if (categoryRepository.existsByName(request.getName())) {
            throw new BusinessException(Translator.toLocale("name_already_exists"), HttpStatus.BAD_REQUEST);
        }
        Category newCategory = new Category();
        newCategory.setName(request.getName());
        newCategory.setStatus(ActiveStatus.ACTIVE);

        return categoryRepository.save(newCategory);
    }

    

     @Override
     public List<Integer> deleteCategory(IdsRequest request) {
         User user = getUser(PermissionKey.DECISION, PermissionType.ACCOUNT);
         List<Integer> categoryIds = request.getIds();
         List<Integer> existingIds = categoryRepository.getAllIdToCheckExist(categoryIds);
         List<Integer> nonExistingIds = categoryIds.stream().filter(id -> !existingIds.contains(id)).toList();
         if (!nonExistingIds.isEmpty()) {
             throw new BusinessException(Translator.toLocale("id_not_exist"), HttpStatus.BAD_REQUEST);
         }
         List<Integer> productIds = productRepository.getAllIdByCategoryId(categoryIds);
         if (!productIds.isEmpty()) {
             productRepository.deleteProducts(productIds);
         }
         categoryRepository.deleteCategories(categoryIds);
         return categoryIds;
     }

    @Override
    public List<CategoryListRes>  updateCategory(UpdateCategoryReq request) {
        User user = getUser(PermissionKey.CREATE, PermissionType.PRODUCT);
        Category currentCategory = categoryRepository.getCategoryToUpdate(request.getId());
        if (currentCategory == null) {
            throw new BusinessException(Translator.toLocale("category_id_not_exist"));
        }
        if (categoryRepository.existsByName(request.getName(), currentCategory.getId())) {
            throw new BusinessException(Translator.toLocale("name_already_exists"), HttpStatus.BAD_REQUEST);
        }
        if (request.getName() != null && !request.getName().isEmpty()) {
            currentCategory.setName(request.getName());
        }
        categoryRepository.save(currentCategory);
        List<Category> categories = categoryRepository.findAll();

        List<CategoryListRes> categoryListRes = categories.stream()
                .map(category -> new CategoryListRes(category.getId(), category.getName(), category.getStatus()))
                .collect(Collectors.toList());
        return categoryListRes;
    }

}
