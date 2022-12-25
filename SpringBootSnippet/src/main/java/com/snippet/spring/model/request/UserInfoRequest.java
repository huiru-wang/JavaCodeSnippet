package com.snippet.spring.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserInfoRequest {

    @JsonProperty("userName")
    private String userName;

}
