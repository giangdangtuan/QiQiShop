package com.app85soft.qiqishop.services.rating;

import com.app85soft.qiqishop.component.Translator;
import com.app85soft.qiqishop.dto.request.rating.AddRatingReq;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.rating.RatingRes;
import com.app85soft.qiqishop.entities.order.Order;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl extends BaseService implements RatingService {
    private final RatingRepository ratingRepository;
    private final OrderRepository orderRepository;

    @Override
    public List<RatingRes> addRating(AddRatingReq req) {
        User user = getUser();

        Order order = orderRepository.findById(req.getOrderId())
                .orElseThrow(() -> new BusinessException("order_not_found"));

        if (order.isRated()) {
            throw new BusinessException(Translator.toLocale("order_has_been_rated"), HttpStatus.BAD_REQUEST);
        }
        if (!orderRepository.existsByUserId(user.getId())) {
            throw new BusinessException(Translator.toLocale("not_your_order"), HttpStatus.BAD_REQUEST);
        }
        if (!orderRepository.existsByOrderIdAndProductId(req.getOrderId(), req.getRatingItems())) {
            throw new BusinessException(Translator.toLocale("model_not_exists_by_order"), HttpStatus.BAD_REQUEST);
        }
        if (ratingRepository.existsByOrderId(req.getOrderId())) {
            throw new BusinessException(Translator.toLocale("item_has_been_rated"), HttpStatus.BAD_REQUEST);
        }

        List<Rating> ratingList = new ArrayList<>();
        for(AddRatingReq.RatingItem item : req.getRatingItems()) {
            Rating rating = new Rating();
            rating.setUserId(user.getId());
            rating.setOrderId(req.getOrderId());
            rating.setProductId(item.getProductId());
            rating.setRatingImage(item.getRatingImage());
            rating.setContent(item.getContent());
            rating.setRatingStar(item.getRatingStar());
            ratingList.add(rating);
        }

        ratingRepository.saveAll(ratingList);
        order.setRated(true);
        orderRepository.save(order);

        List<RatingRes> res = getRatingRes(ratingList);
        return res;
    }

    @Override
    public BaseResponse<List<RatingRes>> getRatings(int productId, int page) {
        long countRating = ratingRepository.countRating(productId);
        double itemRatingStar = ratingRepository.getAverageRating(productId);
        List<RatingRes> listRatings = ratingRepository.getRatings(productId, page);
        return new BaseResponse<>(listRatings, countRating, page, itemRatingStar);
    }

    private List<RatingRes> getRatingRes(List<Rating> ratings) {
        return ratings.stream().map(rating -> RatingRes.builder()
                .id(rating.getId())
                .userId(rating.getUserId())
                .orderId(rating.getOrderId())
                .productId(rating.getProductId())
                .ratingImage(rating.getRatingImage())
                .content(rating.getContent())
                .ratingStar(rating.getRatingStar())
                .createdAt(rating.getCreatedAt())
                .build()
        ).toList();
    }
}
