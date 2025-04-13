package com.app85soft.qiqishop.entities.promotion;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.entities.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "promotion_model")
public class PromotionModel extends BaseEntity {
    @Column(name = "promotion_id")
    private int promotionId;

    @Column(name = "model_id")
    private int modelId;

    @Column(name = "discount_percentage")
    private int discountPercentage;

    @Column(name = "status", columnDefinition = "INT")
    ActiveStatus status;

}
