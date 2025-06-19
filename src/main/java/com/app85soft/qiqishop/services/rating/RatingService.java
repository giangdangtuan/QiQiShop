package com.app85soft.qiqishop.services.rating;

import com.app85soft.qiqishop.dto.request.rating.AddRatingReq;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.rating.RatingRes;

import java.util.List;

public interface RatingService {
    List<RatingRes> addRating(AddRatingReq req);

    BaseResponse<List<RatingRes>> getRatings(int productId, int page);

}
