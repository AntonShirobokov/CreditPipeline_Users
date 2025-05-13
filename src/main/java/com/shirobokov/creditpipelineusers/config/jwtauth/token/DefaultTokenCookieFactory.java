package com.shirobokov.creditpipelineusers.config.jwtauth.token;

import com.shirobokov.creditpipelineusers.entity.User;
import com.shirobokov.creditpipelineusers.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Function;

public class DefaultTokenCookieFactory implements Function<Authentication, Token> {

    private Duration tokenTtl = Duration.ofDays(1);

    private final UserService userService;

    public DefaultTokenCookieFactory(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Token apply(Authentication authentication) {

        var now = Instant.now();

        User user = userService.findByPhone(authentication.getName());

        return new Token(UUID.randomUUID(), String.valueOf(user.getId()), authentication.getAuthorities().
                stream().map(GrantedAuthority::getAuthority).toList(), now, now.plus(tokenTtl));
    }

    public void setTokenTtl(Duration tokenTtl) {
        this.tokenTtl = tokenTtl;
    }

}
