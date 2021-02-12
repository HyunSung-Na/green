package com.green.demo.security;

import com.green.demo.model.user.Email;

import static com.google.common.base.Preconditions.checkNotNull;

public class JwtAuthentication {

  public final Long id;

  public final Email email;

  JwtAuthentication(Long id, Email email) {
    checkNotNull(id, "id must be provided.");
    checkNotNull(email, "email must be provided.");

    this.id = id;
    this.email = email;
  }

}