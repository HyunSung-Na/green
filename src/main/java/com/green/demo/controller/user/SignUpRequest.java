package com.green.demo.controller.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter @Setter
public class SignUpRequest {

    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-]{3,20}$")
    private String name;

    private String principal;

    private String credentials;

    public SignUpRequest(String name, String principal, String credentials) {
        this.name = name;
        this.principal = principal;
        this.credentials = credentials;
    }

}
