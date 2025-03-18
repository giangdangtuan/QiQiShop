package com.app85soft.qiqishop.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CodeListRequest {

    @NotNull(message = "codeList must be required")
    @Size(min = 1)
    private List<String> codeList;

}
