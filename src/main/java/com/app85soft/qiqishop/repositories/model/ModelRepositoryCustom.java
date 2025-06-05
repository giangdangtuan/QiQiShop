package com.app85soft.qiqishop.repositories.model;

import com.app85soft.qiqishop.dto.response.model.ModelRes;
import com.app85soft.qiqishop.entities.model.Model;

import java.util.Collection;
import java.util.List;

public interface ModelRepositoryCustom {
    boolean existsByCode (String code);

    List<ModelRes> getModels(int productId);

    void softDeleteModels(Collection<Model> models);

    void decreaseStock(int modelId, int quantity);
}
