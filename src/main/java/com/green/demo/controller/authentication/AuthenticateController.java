package com.green.demo.controller.authentication;


import com.green.demo.error.NotFoundException;
import com.green.demo.error.UnauthorizedException;
import com.green.demo.model.user.Email;
import com.green.demo.model.user.Role;
import com.green.demo.model.user.User;
import com.green.demo.security.AuthenticationRequest;
import com.green.demo.security.AuthenticationResult;
import com.green.demo.security.Jwt;
import com.green.demo.security.JwtAuthenticationToken;
import com.green.demo.service.user.UserService;
import com.green.demo.util.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static com.green.demo.util.ApiResult.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class AuthenticateController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final Jwt jwt;

    @GetMapping("check-email-token")
    public ApiResult<AuthenticationResultDto> checkEmailToken(String token, String email) {
        User user = userService.findByEmail(new Email(email))
                .orElseThrow(() -> new NotFoundException(User.class, email));
        userService.completeSignUp(user, token);

        String apiToken = user.newApiToken(jwt, new String[]{Role.USER.value()});

        return OK(
                new AuthenticationResultDto(
                        new AuthenticationResult(apiToken, user)));
    }

    @PostMapping("auth")
    public ApiResult<AuthenticationResultDto> authentication(@RequestBody AuthenticationRequest authRequest) throws UnauthorizedException {
        try {
            JwtAuthenticationToken authToken = new JwtAuthenticationToken(authRequest.getPrincipal(), authRequest.getCredentials());
            Authentication authentication = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return OK(
                    new AuthenticationResultDto((AuthenticationResult) authentication.getDetails())
            );
        } catch (AuthenticationException e) {
            throw new UnauthorizedException(e.getMessage());
        }
    }

}
