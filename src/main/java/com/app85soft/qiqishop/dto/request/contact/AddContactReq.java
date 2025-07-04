package com.app85soft.qiqishop.dto.request.contact;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddContactReq {
    @NotBlank
    String name;
    @NotBlank
    String email;
    @NotBlank
    String content;
}
