package com.green.demo.model.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Embeddable;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.regex.Pattern.matches;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email {

  private String address;

  public Email(String address) {
    checkArgument(isNotEmpty(address), "address must be provided.");
    checkArgument(
      address.length() >= 4 && address.length() <= 50,
      "address length must be between 4 and 50 characters."
    );
    checkArgument(checkAddress(address), "Invalid email address: " + address);

    this.address = address;
  }

  private static boolean checkAddress(String address) {
    return matches("[\\w~\\-.+]+@[\\w~\\-]+(\\.[\\w~\\-]+)+", address);
  }

  public String emailName() {
    String[] tokens = address.split("@");
    if (tokens.length == 2)
      return tokens[0];
    return null;
  }

  public String emailDomain() {
    String[] tokens = address.split("@");
    if (tokens.length == 2)
      return tokens[1];
    return null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Email email = (Email) o;
    return Objects.equals(address, email.address);
  }

  @Override
  public int hashCode() {
    return Objects.hash(address);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
      .append("address", address)
      .toString();
  }

}