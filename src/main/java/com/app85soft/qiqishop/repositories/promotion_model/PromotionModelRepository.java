package com.app85soft.qiqishop.repositories.promotion_model;

import com.app85soft.qiqishop.entities.promotion.PromotionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionModelRepository extends JpaRepository<PromotionModel, Integer>, PromotionModelRepositoryCustom {
    List<PromotionModel> findAllByPromotionId(int productId);

}
