package com.app85soft.qiqishop.dto.response.ghn;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GhnCreateOrderRes {
    private String order_code;
    private int status_code;
    private String message;
}
