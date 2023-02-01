package com.snippet.spring.model;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserCache {

    @NotBlank
    private String username;

    @Valid
    @NotNull
    private NestingObj nestingObj;
}
