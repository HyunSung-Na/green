package com.green.demo.security;

import com.green.demo.model.Id;
import com.green.demo.model.user.Email;
import com.green.demo.model.user.User;

import static com.google.common.base.Preconditions.checkNotNull;

public class JwtAuthentication {

  public final Id<User, Long> id;

  public final String name;

  public final Email email;

  JwtAuthentication(Long id, String name, Email email) {
    checkNotNull(id, "id must be provided.");
    checkNotNull(name, "name must be provided.");
    checkNotNull(email, "email must be provided.");

    this.id = Id.of(User.class, id);
    this.name = name;
    this.email = email;
  }

}