package com.green.demo.controller.user;

import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Getter
public class JoinRequest {

    private String principal;

    private String credentials;

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("principal", principal)
                .append("credentials", credentials)
                .toString();
    }
}
