package com.app85soft.qiqishop.repositories.rating;

import com.app85soft.qiqishop.entities.rating.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer>, RatingRepositoryCustom {
    boolean existsByOrderId(Integer orderId);
}
