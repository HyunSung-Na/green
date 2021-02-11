package com.green.demo.controller.authentication;

import com.green.demo.controller.user.UserDto;
import com.green.demo.security.AuthenticationResult;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
public class AuthenticationResultDto {

  private String jwtToken;

  private UserDto userDto;

  public AuthenticationResultDto(AuthenticationResult details) {
    this.jwtToken = details.getApiToken();
    this.userDto = UserDto.of(details.getUser());
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
      .append("jwtToken", jwtToken)
      .append("userDto", userDto)
      .toString();
  }

}