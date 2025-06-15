package com.app85soft.qiqishop.dto.response.rating;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RatingRes {
    int id;
    Integer userId;
    String userName;
    Integer orderId;
    Integer modelId;
    String modelName;
    Integer ratingImage;
    String content;
    Integer ratingStar;
    String originUrl;
    String thumbUrl;

    Date createdAt;
}
