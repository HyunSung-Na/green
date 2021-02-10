package com.green.demo.controller.user;

import com.green.demo.model.user.Email;
import com.green.demo.model.user.User;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDateTime;

@Getter
public class UserDto {

    private Long id;

    private String name;

    private Email email;

    private String profileImageUrl;

    private String emailCheckToken;

    private LocalDateTime emailCheckTokenGenDate;

    private boolean emailVerified;

    private int loginCount;

    private LocalDateTime lastLoginAt;

    private LocalDateTime createAt;

    @Builder
    private UserDto(Long id, String name, Email email, String profileImageUrl, String emailCheckToken,
                   LocalDateTime emailCheckTokenGenDate, boolean emailVerified,
                   int loginCount, LocalDateTime lastLoginAt, LocalDateTime createAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.emailCheckToken = emailCheckToken;
        this.emailCheckTokenGenDate = emailCheckTokenGenDate;
        this.emailVerified = emailVerified;
        this.loginCount = loginCount;
        this.lastLoginAt = lastLoginAt;
        this.createAt = createAt;
    }

    public static UserDto of(User user) {
        return UserDto.builder()
                .id(user.getSeq())
                .name(user.getName())
                .email(user.getEmail())
                .profileImageUrl(user.getProfileImageUrl())
                .emailCheckToken(user.getEmailCheckToken())
                .emailCheckTokenGenDate(user.getEmailCheckTokenGenDate())
                .emailVerified(user.isEmailVerified())
                .loginCount(user.getLoginCount())
                .lastLoginAt(user.getLastLoginAt())
                .createAt(user.getCreateAt())
                .build();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("email", email)
                .append("profileImageUrl", profileImageUrl)
                .append("emailCheckToken", emailCheckToken)
                .append("emailCheckTokenGenDate", emailCheckTokenGenDate)
                .append("emailVerified", emailVerified)
                .append("loginCount", loginCount)
                .append("lastLoginAt", lastLoginAt)
                .append("createAt", createAt)
                .toString();
    }
}
