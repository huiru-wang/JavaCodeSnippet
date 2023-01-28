package com.snippet.spring.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateUserInfoRequest {

    @NotBlank
    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("phone")
    private String phone;
}
