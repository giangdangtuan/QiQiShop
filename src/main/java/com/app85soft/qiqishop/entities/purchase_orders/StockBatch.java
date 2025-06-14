package com.app85soft.qiqishop.entities.purchase_orders;

import com.app85soft.qiqishop.entities.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "stock_batches")
public class StockBatch extends BaseEntity {
    private int modelId;

    private int purchaseOrderItemId;

    private Integer quantityReceived;

    private Integer quantityRemaining;

    private BigDecimal unitCost;
}
