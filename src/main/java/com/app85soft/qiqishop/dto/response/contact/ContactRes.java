package com.app85soft.qiqishop.dto.response.contact;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContactRes {
    int id;
    String name;
    String email;
    String content;
    Date createdAt;
}
