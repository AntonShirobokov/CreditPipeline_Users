package com.shirobokov.creditpipelineusers.controller;


import com.shirobokov.creditpipelineusers.config.jwtauth.token.TokenUser;
import com.shirobokov.creditpipelineusers.config.security.MyUserDetails;
import com.shirobokov.creditpipelineusers.entity.Application;
import com.shirobokov.creditpipelineusers.entity.Passport;
import com.shirobokov.creditpipelineusers.entity.User;
import com.shirobokov.creditpipelineusers.service.UserService;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UserController {

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        TokenUser tokenUser = (TokenUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.findById(Integer.parseInt(tokenUser.getToken().subject()));
        Passport passport = user.getPassport();

        model.addAttribute("user", user);
        model.addAttribute("passport", passport);

        return "profile";
    }

    @GetMapping("/applications")
    public String applications(Model model) {

        return "applications";
    }


}
