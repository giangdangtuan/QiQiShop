package com.app85soft.qiqishop.repositories.promotion;

import com.app85soft.qiqishop.entities.promotion.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Integer>, PromotionRepositoryCustom {

}
