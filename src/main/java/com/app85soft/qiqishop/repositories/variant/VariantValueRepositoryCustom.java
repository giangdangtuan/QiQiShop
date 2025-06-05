package com.app85soft.qiqishop.repositories.variant;

import com.app85soft.qiqishop.entities.model.VariantValue;

import java.util.List;

public interface VariantValueRepositoryCustom {
    List<VariantValue> findByProductId(Integer productId);
}
