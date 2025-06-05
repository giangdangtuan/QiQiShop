package com.app85soft.qiqishop.repositories.rating;

import com.app85soft.qiqishop.dto.response.rating.RatingRes;

import java.util.List;

public interface RatingRepositoryCustom {

    long countRating(int productId);

    List<RatingRes> getRatings(int productId, int page);

    Double getAverageRating(int productId);

}
