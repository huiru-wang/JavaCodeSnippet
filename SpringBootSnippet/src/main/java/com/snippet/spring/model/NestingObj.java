package com.snippet.spring.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class NestingObj {

    @NotBlank
    private String info;
}
