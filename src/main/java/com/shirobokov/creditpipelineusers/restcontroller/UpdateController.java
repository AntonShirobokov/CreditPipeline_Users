package com.shirobokov.creditpipelineusers.restcontroller;


import com.shirobokov.creditpipelineusers.config.jwtauth.token.Token;
import com.shirobokov.creditpipelineusers.config.jwtauth.token.TokenUser;
import com.shirobokov.creditpipelineusers.entity.Passport;
import com.shirobokov.creditpipelineusers.entity.User;
import com.shirobokov.creditpipelineusers.service.PassportService;
import com.shirobokov.creditpipelineusers.service.UserService;
import com.shirobokov.creditpipelineusers.util.PassportValidator;
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

    private final PassportService passportService;

    private final UserValidator userValidator;

    private final PassportValidator passportValidator;

    public UpdateController(UserService userService, PassportService passportService, UserValidator userValidator, PassportValidator passportValidator) {
        this.userService = userService;
        this.passportService = passportService;
        this.userValidator = userValidator;
        this.passportValidator = passportValidator;
    }


    @PostMapping("/api/user/update-contact")
    @ResponseBody
    public ResponseEntity<?> updateContact(@RequestBody @Validated User updatedUser, BindingResult bindingResult) {

        TokenUser tokenUser = (TokenUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User currentUser = userService.findById(Integer.parseInt(tokenUser.getToken().subject())); // авторизованный пользователь

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
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/api/user/update-passport")
    @ResponseBody
    public ResponseEntity<?> updatePassport(@RequestBody @Validated Passport updatedPassport, BindingResult bindingResult) {
        System.out.println("Новые данные паспотра" + updatedPassport);

        TokenUser tokenUser = (TokenUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User currentUser = userService.findById(Integer.parseInt(tokenUser.getToken().subject()));

        Passport oldPassport = currentUser.getPassport();

        System.out.println("Старый паспорт" + updatedPassport);

        passportValidator.validate(updatedPassport, bindingResult);

        if (bindingResult.hasErrors()) {
            Map<String, String> errors= new HashMap<>();

            bindingResult.getFieldErrors().forEach(err ->
                    errors.put(err.getField(), err.getDefaultMessage()));

            return ResponseEntity.badRequest().body(Map.of("errors", errors));
        }

        passportService.updatePassport(oldPassport, updatedPassport);

        return ResponseEntity.ok(updatedPassport);
    }



}
