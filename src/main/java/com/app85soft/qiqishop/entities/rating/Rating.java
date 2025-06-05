package com.app85soft.qiqishop.entities.rating;

import com.app85soft.qiqishop.entities.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "ratings")
public class Rating extends BaseEntity {
    Integer userId;
    Integer orderId;
    Integer modelId;
    Integer ratingImage;

    String content;
    Integer ratingStar;

    boolean deleted;
}
