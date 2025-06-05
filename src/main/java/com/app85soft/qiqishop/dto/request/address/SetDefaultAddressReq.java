package com.app85soft.qiqishop.dto.request.address;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SetDefaultAddressReq {
    @NotNull
    Integer id;
}
