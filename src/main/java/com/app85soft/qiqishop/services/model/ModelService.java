package com.app85soft.qiqishop.services.model;

import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.model.ModelRes;

public interface ModelService {
    BaseResponse<ModelRes> getModelDetail(int modelId);
}
