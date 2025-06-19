package com.app85soft.qiqishop.repositories.product;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.dto.response.product.ProductDetailRes;
import com.app85soft.qiqishop.dto.response.product.ProductRes;
import com.app85soft.qiqishop.entities.product.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepositoryCustom {

    boolean existsByCode (String code);

    boolean existsByName(String name, Integer id);

    long countProduct(ActiveStatus status, String searchKeyword, List<Integer> categoryId, BigDecimal startPrice, BigDecimal endPrice);

    List<ProductRes> getProduct(ActiveStatus status, String searchKeyword, List<Integer> categoryId, BigDecimal startPrice, BigDecimal endPrice, int page);

    List<ProductRes> getProductSale(ActiveStatus status, String searchKeyword, Integer categoryId, int page);

    ProductDetailRes getProductDetail(int productId);

    Product getProductToUpdate(int id);

    List<Integer> getAllIdToCheckExist(List<Integer> productIds);

    List<Integer> getAllIdByCategoryId(List<Integer> categoryId);

    void deleteProducts(List<Integer> productIds);


}
