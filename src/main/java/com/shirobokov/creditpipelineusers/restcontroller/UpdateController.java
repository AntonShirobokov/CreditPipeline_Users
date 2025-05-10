package com.shirobokov.creditpipelineusers.restcontroller;


import com.shirobokov.creditpipelineusers.config.jwtauth.token.Token;
import com.shirobokov.creditpipelineusers.config.jwtauth.token.TokenUser;
import com.shirobokov.creditpipelineusers.entity.User;
import com.shirobokov.creditpipelineusers.service.UserService;
import com.shirobokov.creditpipelineusers.util.UserValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UpdateController {

    private final UserService userService;

    private final UserValidator userValidator;

    public UpdateController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }


    @PostMapping("/api/user/update-contact")
    @ResponseBody
    public ResponseEntity<?> updateContact(@RequestBody @Validated User updatedUser, BindingResult bindingResult) {

        TokenUser tokenUser = (TokenUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User currentUser = userService.findByPhone(tokenUser.getUsername()); // авторизованный пользователь

        if (!currentUser.getPhone().equals(updatedUser.getPhone())  && userService.findByPhone(updatedUser.getPhone()) != null) {
            bindingResult.rejectValue("phone", "error.phoneAlreadyExists", "Такой номер телефона уже зарегестрирован");
        }

        userValidator.validate(updatedUser, bindingResult);
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(err ->
                    errors.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(Map.of("errors", errors));
        }


        userService.updateUser(currentUser, updatedUser);
        //updateSecurityContext(tokenUser, updatedUser);
        return ResponseEntity.ok(currentUser);
    }



//    private void updateSecurityContext(TokenUser oldTokenUser, User updatedUser) {
//        Token oldToken = oldTokenUser.getToken();
//
//        // Создаём новый Token с обновлённым subject (телефоном)
//        Token newToken = new Token(
//                oldToken.id(),                    // тот же UUID
//                updatedUser.getPhone(),           // обновлённый subject
//                oldToken.authorities(),           // те же права
//                oldToken.createdAt(),             // прежнее время создания
//                oldToken.expiresAt()              // прежнее время истечения
//        );
//
//        // Создаём нового TokenUser с новым токеном и обновлённым subject
//        TokenUser newTokenUser = new TokenUser(
//                newToken.subject(),               // новый username (телефон)
//                "nopassword",                     // пароль не используется
//                oldTokenUser.isEnabled(),
//                oldTokenUser.isAccountNonExpired(),
//                oldTokenUser.isCredentialsNonExpired(),
//                oldTokenUser.isAccountNonLocked(),
//                oldTokenUser.getAuthorities(),
//                newToken
//        );
//
//        // Создаём новое Authentication
//        Authentication newAuth = new UsernamePasswordAuthenticationToken(
//                newTokenUser,
//                null,
//                newTokenUser.getAuthorities()
//        );
//
//        // Обновляем SecurityContext
//        SecurityContextHolder.getContext().setAuthentication(newAuth);
//    }

}
