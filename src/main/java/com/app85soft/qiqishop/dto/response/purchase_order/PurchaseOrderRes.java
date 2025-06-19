package com.app85soft.qiqishop.dto.response.purchase_order;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PurchaseOrderRes {
    int id;
    String code;
    Integer userId;
    String userName;
    String note;
    Long importDate;
    Date createdAt;

    List<PurchaseOrderItemRes> items;
}
