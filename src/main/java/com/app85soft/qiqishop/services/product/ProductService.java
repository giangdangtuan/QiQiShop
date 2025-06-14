package com.app85soft.qiqishop.services.product;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.dto.request.IdsRequest;
import com.app85soft.qiqishop.dto.request.product.AddProductReq;
import com.app85soft.qiqishop.dto.request.product.UpdateProductReq;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.product.ProductDetailRes;
import com.app85soft.qiqishop.dto.response.product.ProductRes;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    ProductRes addProduct(AddProductReq productReq);
    ProductRes updateProduct(UpdateProductReq product);
    List<Integer> deleteProducts(IdsRequest req);
    BaseResponse<List<ProductRes>> getProducts(ActiveStatus status, String name, Integer categoryId, BigDecimal startPrice, BigDecimal endPrice, String sortBy, int page);
    BaseResponse<ProductDetailRes> getProduct(int productId);

}
