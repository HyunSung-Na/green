package com.green.demo.model.user;

import com.green.demo.security.Jwt;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.time.LocalDateTime.now;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Entity
@Getter
@NoArgsConstructor
public class User {

  @Id
  @GeneratedValue
  private Long seq;

  @Column
  private String name;

  @Column
  @Enumerated
  private Email email;

  @Column
  private String password;

  @Column
  private String profileImageUrl;

  private String emailCheckToken;

  private LocalDateTime emailCheckTokenGenDate;

  private boolean emailVerified;

  @Column
  private int loginCount;

  @Column
  private LocalDateTime lastLoginAt;

  @Column
  private LocalDateTime createAt;

  @Builder
  public User(String name, Email email, String password) {
    this(name, email, password, null);
  }

  @Builder
  public User(String name, Email email, String password, String profileImageUrl) {
    this(null, name, email, password, profileImageUrl, 0, null, null);
  }

  @Builder
  public User(Long seq, String name, Email email, String password, String profileImageUrl, int loginCount, LocalDateTime lastLoginAt, LocalDateTime createAt) {
    checkArgument(isNotEmpty(name), "name must be provided.");
    checkArgument(
      name.length() >= 1 && name.length() <= 10,
      "name length must be between 1 and 10 characters."
    );
    checkNotNull(email, "email must be provided.");
    checkNotNull(password, "password must be provided.");
    checkArgument(
      profileImageUrl == null || profileImageUrl.length() <= 255,
      "profileImageUrl length must be less than 255 characters."
    );

    this.seq = seq;
    this.name = name;
    this.email = email;
    this.password = password;
    this.profileImageUrl = profileImageUrl;
    this.loginCount = loginCount;
    this.lastLoginAt = lastLoginAt;
    this.createAt = defaultIfNull(createAt, now());
  }

  public void login(PasswordEncoder passwordEncoder, String credentials) {
    if (!passwordEncoder.matches(credentials, password))
      throw new IllegalArgumentException("Bad credential");
  }

  public void generateEmailCheckToken() {
    this.emailCheckToken = UUID.randomUUID().toString();
    this.emailCheckTokenGenDate = LocalDateTime.now();
  }

  public boolean isValidToken(String token) {
    checkArgument(
            token == null, "token must be provided");

    return this.emailCheckToken.equals(token);
  }

  public void afterLoginSuccess() {
    loginCount++;
    lastLoginAt = now();
  }

  public String newApiToken(Jwt jwt, String[] roles) {
    Jwt.Claims claims = Jwt.Claims.of(seq, name, email, roles);
    return jwt.newToken(claims);
  }

  public void updateProfileImage(String profileImageUrl) {
    checkArgument(
      profileImageUrl == null || profileImageUrl.length() <= 255,
      "profileImageUrl length must be less than 255 characters."
    );

    this.profileImageUrl = profileImageUrl;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return Objects.equals(seq, user.seq);
  }

  @Override
  public int hashCode() {
    return Objects.hash(seq);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
      .append("seq", seq)
      .append("name", name)
      .append("email", email)
      .append("password", "[PROTECTED]")
      .append("profileImageUrl", profileImageUrl)
      .append("loginCount", loginCount)
      .append("lastLoginAt", lastLoginAt)
      .append("createAt", createAt)
      .toString();
  }

}