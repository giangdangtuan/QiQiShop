package com.app85soft.qiqishop.services.category;

import java.util.List;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.dto.request.IdsRequest;
import com.app85soft.qiqishop.dto.request.category.AddCategoryReq;
import com.app85soft.qiqishop.dto.request.category.UpdateCategoryReq;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.category.CategoryListRes;
import com.app85soft.qiqishop.entities.category.Category;

public interface CategoryService {
    BaseResponse<List<CategoryListRes>> getCategories(ActiveStatus status, String searchKeyword, int page);

    Category addCategory(AddCategoryReq request);

    BaseResponse<List<CategoryListRes>>  updateCategory(UpdateCategoryReq request,
    ActiveStatus status, String searchKeyword, int page);

    List<Integer> deleteCategory(IdsRequest request);

}
