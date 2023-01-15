package com.snippet.spring.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserInfoRequest {

    @NotBlank
    @JsonProperty("userName")
    private String userName;

}
