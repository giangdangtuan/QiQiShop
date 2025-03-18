package com.app85soft.qiqishop.dto.request.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddUserReq extends AddUserBaseReq {
    int companyId;
}
