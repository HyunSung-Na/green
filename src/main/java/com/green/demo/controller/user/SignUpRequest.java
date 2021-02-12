package com.green.demo.controller.user;

import com.green.demo.model.Name;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SignUpRequest {

    private Name name;

    private String principal;

    private String credentials;

    public SignUpRequest(Name name, String principal, String credentials) {
        this.name = name;
        this.principal = principal;
        this.credentials = credentials;
    }

}
