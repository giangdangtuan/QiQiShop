package com.app85soft.qiqishop.services.promotion;

import com.app85soft.qiqishop.component.Translator;
import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.dto.request.IdsRequest;
import com.app85soft.qiqishop.dto.request.promotion.AddPromotionReq;
import com.app85soft.qiqishop.dto.request.promotion.UpdatePromotionReq;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.promotion.PromotionRes;
import com.app85soft.qiqishop.entities.promotion.Promotion;
import com.app85soft.qiqishop.entities.promotion.PromotionModel;
import com.app85soft.qiqishop.entities.role.constant.PermissionKey;
import com.app85soft.qiqishop.entities.role.constant.PermissionType;
import com.app85soft.qiqishop.entities.user.User;
import com.app85soft.qiqishop.exceptions.BusinessException;
import com.app85soft.qiqishop.repositories.promotion.PromotionRepository;
import com.app85soft.qiqishop.repositories.promotion_model.PromotionModelRepository;
import com.app85soft.qiqishop.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl extends BaseService implements PromotionService {
    private final PromotionRepository promotionRepository;
    private final PromotionModelRepository promotionModelRepository;

    @Override
    public PromotionRes addPromotion(AddPromotionReq promotionReq) {
        User user = getUser(PermissionKey.CREATE, PermissionType.PRODUCT);
        long startTime = promotionReq.getStartTime();
        long endTime = promotionReq.getEndTime();

        for (AddPromotionReq.PromotionModel item : promotionReq.getPromotionModels()) {
            List<PromotionModel> conflicts = promotionModelRepository.findConflictingPromotions(
                    item.getModelId(),
                    startTime,
                    endTime
            );
            if (!conflicts.isEmpty()) {
                throw new BusinessException(Translator.toLocale("Model_" + item.getModelId() + "_has_promotion_on_this_time"), HttpStatus.BAD_REQUEST);
            }
        }

        Promotion promotion = new Promotion();
        promotion.setName(promotionReq.getName());
        promotion.setStartTime(startTime);
        promotion.setEndTime(endTime);
        promotion.setStatus(ActiveStatus.ACTIVE);
        promotionRepository.save(promotion);

        List<PromotionModel> promotionModels = new ArrayList<>();
        for (AddPromotionReq.PromotionModel item : promotionReq.getPromotionModels()) {
            PromotionModel promotionModel = new PromotionModel();
            promotionModel.setPromotionId(promotion.getId());
            promotionModel.setModelId(item.getModelId());
            promotionModel.setDiscountPercentage(item.getDiscountPercentage());
            promotionModel.setStatus(ActiveStatus.ACTIVE);
            promotionModels.add(promotionModel);
        }
        promotionModelRepository.saveAll(promotionModels);

        return getPromotionRes(promotion, promotionModels);
    }


    @Override
    public PromotionRes updatePromotion(UpdatePromotionReq promotionReq) {
        User user = getUser(PermissionKey.CREATE, PermissionType.PRODUCT);
        Promotion promotion = promotionRepository.getPromotionToUpdate(promotionReq.getId());

        if (promotion == null) {
            throw new BusinessException(Translator.toLocale("promotion_id_not_exist"));
        }
        if (promotionReq.getName() != null && !promotionReq.getName().isEmpty()) {
            promotion.setName(promotionReq.getName());
        }
        if (promotionReq.getStartTime() != null) {
            promotion.setStartTime(promotionReq.getStartTime());
        }
        if (promotionReq.getEndTime() != null) {
            promotion.setEndTime(promotionReq.getEndTime());
        }
        if (promotionReq.getStatus() != null) {
            promotion.setStatus(promotionReq.getStatus());
        }
        promotionRepository.save(promotion);

        List<PromotionModel> existingPromotionModels = promotionModelRepository.findAllByPromotionId(promotion.getId());
        Map<Integer, PromotionModel> existingPromotionModelMap = existingPromotionModels.stream()
                .collect(Collectors.toMap(PromotionModel::getModelId, m -> m));

        List<PromotionModel> updatedPromotionModels = new ArrayList<>();

        for (UpdatePromotionReq.PromotionModel item : promotionReq.getPromotionModels()) {
            PromotionModel promotionModel = existingPromotionModelMap.remove(item.getModelId());

            if (promotionModel == null) {
                promotionModel = new PromotionModel();
                promotionModel.setPromotionId(promotion.getId());
                promotionModel.setModelId(item.getModelId());
            }
            promotionModel.setDiscountPercentage(item.getDiscountPercentage());
            promotionModel.setStatus(item.getStatus());

            updatedPromotionModels.add(promotionModel);
        }
        if (!existingPromotionModelMap.isEmpty()) {
            promotionModelRepository.softDeletePromotionModels(existingPromotionModelMap.values());
        }
        promotionModelRepository.saveAll(updatedPromotionModels);

        PromotionRes promotionRes = getPromotionRes(promotion, updatedPromotionModels);
        return promotionRes;
    }

    @Override
    public List<Integer> deletePromotions(IdsRequest req) {
        User user = getUser(PermissionKey.DECISION, PermissionType.PRODUCT);
        List<Integer> promotionIds = req.getIds();
        List<Integer> existingIds = promotionRepository.getAllIdToCheckExist(promotionIds);
        List<Integer> nonExistingIds = promotionIds.stream().filter(id -> !existingIds.contains(id)).toList();
        if (!nonExistingIds.isEmpty()) {
            throw new BusinessException(Translator.toLocale("id_not_exist"), HttpStatus.BAD_REQUEST);
        }
        promotionRepository.deletePromotions(promotionIds);
        return promotionIds;
    }

    @Override
    public BaseResponse<List<PromotionRes>> getPromotions(ActiveStatus status, String name, Long startTime, Long
            endTime, int page) {
        User user = getUser(PermissionKey.READ, PermissionType.PRODUCT);
        long countPromotion = promotionRepository.countPromotion(status, name, startTime, endTime);
        List<PromotionRes> listPromotions = promotionRepository.getPromotions(status, name, startTime, endTime, page);
        return new BaseResponse<>(listPromotions, countPromotion, page);
    }

    @Override
    public BaseResponse<PromotionRes> getPromotion(int promotionId) {
        User user = getUser(PermissionKey.READ, PermissionType.PRODUCT);

        PromotionRes promotionRes = promotionRepository.getPromotionDetail(promotionId);
        if (promotionRes == null) {
            throw new BusinessException(Translator.toLocale("id_not_exist"), HttpStatus.NOT_FOUND);
        }
        return new BaseResponse<>(promotionRes);
    }

    private PromotionRes getPromotionRes(Promotion promotion, List<PromotionModel> promotionModels) {

        return PromotionRes.builder()
                .id(promotion.getId())
                .name(promotion.getName())
                .startTime(promotion.getStartTime())
                .endTime(promotion.getEndTime())
                .status(promotion.getStatus())
                .promotionModels(promotionModelRepository.getPromotionModels(promotion.getId()))
                .build();
    }
}
