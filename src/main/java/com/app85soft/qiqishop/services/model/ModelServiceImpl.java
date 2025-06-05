package com.app85soft.qiqishop.services.model;

import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.model.ModelRes;
import com.app85soft.qiqishop.entities.model.Model;
import com.app85soft.qiqishop.repositories.model.ModelRepository;
import com.app85soft.qiqishop.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModelServiceImpl extends BaseService implements ModelService {
    private final ModelRepository modelRepository;
    @Override
    public BaseResponse<ModelRes> getModelDetail(int modelId) {
        Model model = modelRepository.getById(modelId);
        ModelRes modelRes = new ModelRes();
        modelRes.setName(model.getName());
        modelRes.setCode(model.getCode());
        modelRes.setId(model.getId());
        modelRes.setOriginalPrice(model.getPrice());
        modelRes.setStock(model.getStock());
        modelRes.setOptionValue1Id(model.getOptionValue1Id());
        modelRes.setOptionValue2Id(model.getOptionValue2Id());
        modelRes.setProductId(model.getProductId());
        modelRes.setSoldCount(model.getSoldCount());
        return new BaseResponse<>(modelRes);
    }
}
