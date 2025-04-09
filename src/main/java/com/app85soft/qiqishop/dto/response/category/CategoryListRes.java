package com.app85soft.qiqishop.dto.response.category;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryListRes {
    int id;
    String name;
    ActiveStatus status;
}
