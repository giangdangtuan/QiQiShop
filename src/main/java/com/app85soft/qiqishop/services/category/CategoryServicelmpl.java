package com.app85soft.qiqishop.services.category;

import java.util.List;

import org.springframework.stereotype.Service;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.dto.request.IdsRequest;
import com.app85soft.qiqishop.dto.request.category.AddCategoryReq;
import com.app85soft.qiqishop.dto.request.category.UpdateCategoryReq;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.category.CategoryListRes;
import com.app85soft.qiqishop.dto.response.user.UserListRes;
import com.app85soft.qiqishop.entities.category.Category;
import com.app85soft.qiqishop.entities.role.constant.PermissionKey;
import com.app85soft.qiqishop.entities.role.constant.PermissionType;
import com.app85soft.qiqishop.entities.user.User;
import com.app85soft.qiqishop.repositories.category.CategoryRepository;
import com.app85soft.qiqishop.services.BaseService;
import com.app85soft.qiqishop.services.category.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServicelmpl extends BaseService implements CategoryService {

    private final CategoryRepository categoryRepository;

    public BaseResponse<List<CategoryListRes>> getCategories(ActiveStatus status, String searchKeyword, int page) {
        User user = getUser(PermissionKey.READ, PermissionType.PRODUCT);

        long count = categoryRepository.countCategory(status, searchKeyword, user.getRole());
        List<UserListRes> users = userRepository.getUsers(status, searchKeyword, page, user.getRole());
        return new BaseResponse<>(users, count, page);
    }

    @Override
    public Category addCategory(AddCategoryReq request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addCategory'");
    }

    @Override
    public CategoryListRes updateCategory(UpdateCategoryReq request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateCategory'");
    }

    @Override
    public List<Integer> deleteCategory(IdsRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteCategory'");
    }

}
