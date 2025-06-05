package com.app85soft.qiqishop.repositories.product;

import com.app85soft.qiqishop.dto.response.product.ProductImageRes;
import com.app85soft.qiqishop.entities.product.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
    List<ProductImage> findByProductId(int productId);
}
