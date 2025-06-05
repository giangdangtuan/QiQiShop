package com.app85soft.qiqishop.dto.request.address;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DivisionReq {
    Integer parentId;
    Integer level;
}
