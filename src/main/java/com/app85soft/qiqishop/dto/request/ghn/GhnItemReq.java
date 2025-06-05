package com.app85soft.qiqishop.dto.request.ghn;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GhnItemReq {
    String name;
    String code;
    int quantity;
    int weight;
}
