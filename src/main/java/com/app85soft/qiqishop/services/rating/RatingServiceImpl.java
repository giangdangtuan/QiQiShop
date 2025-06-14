package com.app85soft.qiqishop.services.rating;

import com.app85soft.qiqishop.component.Translator;
import com.app85soft.qiqishop.dto.request.rating.AddRatingReq;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.rating.RatingRes;
import com.app85soft.qiqishop.entities.rating.Rating;
import com.app85soft.qiqishop.entities.user.User;
import com.app85soft.qiqishop.exceptions.BusinessException;
import com.app85soft.qiqishop.repositories.order.OrderDetailRepository;
import com.app85soft.qiqishop.repositories.order.OrderRepository;
import com.app85soft.qiqishop.repositories.rating.RatingRepository;
import com.app85soft.qiqishop.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl extends BaseService implements RatingService {
    private final RatingRepository ratingRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Override
    public RatingRes addRating(AddRatingReq req) {
        User user = getUser();

        if (!orderRepository.existsByUserId(user.getId())) {
            throw new BusinessException(Translator.toLocale("not_your_order"), HttpStatus.BAD_REQUEST);
        }
        if (!orderDetailRepository.existsByOrderIdAndModelId(req.getOrderId(), req.getModelId())) {
            throw new BusinessException(Translator.toLocale("model_not_exists_by_order"), HttpStatus.BAD_REQUEST);
        }
        if (ratingRepository.existsByOrderIdAndModelId(req.getOrderId(), req.getModelId())) {
            throw new BusinessException(Translator.toLocale("item_has_been_rated"), HttpStatus.BAD_REQUEST);
        }
        Rating rating = new Rating();
        rating.setUserId(user.getId());
        rating.setOrderId(req.getOrderId());
        rating.setModelId(req.getModelId());
        rating.setRatingImage(req.getRatingImage());
        rating.setContent(req.getContent());
        rating.setRatingStar(req.getRatingStar());

        ratingRepository.save(rating);
        RatingRes res = getRatingRes(rating);
        return res;
    }

    @Override
    public BaseResponse<List<RatingRes>> getRatings(int productId, int page) {
        long countRating = ratingRepository.countRating(productId);
        double itemRatingStar = ratingRepository.getAverageRating(productId);
        List<RatingRes> listRatings = ratingRepository.getRatings(productId, page);
        return new BaseResponse<>(listRatings, countRating, page, itemRatingStar);
    }

    private RatingRes getRatingRes(Rating rating) {

        return RatingRes.builder()
                .id(rating.getId())
                .userId(rating.getUserId())
                .orderId(rating.getOrderId())
                .modelId(rating.getModelId())
                .ratingImage(rating.getRatingImage())
                .content(rating.getContent())
                .ratingStar(rating.getRatingStar())
                .createdAt(rating.getCreatedAt())
                .build();
    }
}
