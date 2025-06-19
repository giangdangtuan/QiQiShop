package com.app85soft.qiqishop.entities.purchase_orders;

import com.app85soft.qiqishop.entities.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
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
@Table(name = "purchase_orders")
public class PurchaseOrder extends BaseEntity {
    String code;
    int userId;
    String note;

    @Column(name = "import_date")
    Long importDate;
}
