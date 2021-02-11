package com.green.demo.controller.user;


import com.green.demo.error.NotFoundException;
import com.green.demo.error.UnauthorizedException;
import com.green.demo.model.user.Email;
import com.green.demo.model.user.Role;
import com.green.demo.model.user.User;
import com.green.demo.security.Jwt;
import com.green.demo.security.JwtAuthentication;
import com.green.demo.service.user.UserService;
import com.green.demo.util.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    private final Jwt jwt;

    @PostMapping("join")
    public ApiResult<JoinResult> join(@RequestBody SignUpRequest signUpRequest) {
        User newUser = userService.join(signUpRequest.getName(),
                                        new Email(signUpRequest.getPrincipal()),
                                        signUpRequest.getCredentials());

        String apiToken = newUser.newApiToken(jwt, new String[]{Role.USER.value()});

        return ApiResult.OK(new JoinResult(apiToken, newUser));
    }

    @GetMapping("user/info/{userId}")
    public ApiResult<UserDto> info(@PathVariable Long userId, @AuthenticationPrincipal JwtAuthentication jwtAuthentication) {
        if (!jwtAuthentication.id.equals(userId)) {
            throw new UnauthorizedException("AuthenticationFailed id");
        }

        return ApiResult.OK(
            userService.findById(userId)
                .map(UserDto::of)
                    .orElseThrow(() -> new NotFoundException(User.class, userId)));
    }

    @GetMapping("user/info/list")
    public ApiResult<List<UserDto>> infoList(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return ApiResult.OK(
            userService.users(pageRequest).stream()
                .map(UserDto::of)
                .collect(toList())
        );
    }

    @PostMapping("user/exists")
    public ApiResult<Boolean> checkEmail(Map<String, String> request) {
        String email = request.get("email");
        return ApiResult.OK(userService.checkFindByEmail(new Email(email)).isPresent());
    }

    @PostMapping("user/settings/password")
    public ResponseEntity<?> updatePassword(@AuthenticationPrincipal JwtAuthentication jwtAuthentication, String newPassword) {
        userService.updatePassword(jwtAuthentication, newPassword);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("user/remove/{userId}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long userId, @AuthenticationPrincipal JwtAuthentication jwtAuthentication) {

        if (!jwtAuthentication.id.equals(userId)) {
            throw new UnauthorizedException("AuthenticationFailed id");
        }

        userService.deleteById(userId);
        return ResponseEntity.ok().build();
    }
}
