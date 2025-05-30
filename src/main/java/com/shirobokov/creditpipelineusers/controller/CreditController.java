package com.shirobokov.creditpipelineusers.controller;


import com.shirobokov.creditpipelineusers.config.jwtauth.token.TokenUser;
import com.shirobokov.creditpipelineusers.entity.Passport;
import com.shirobokov.creditpipelineusers.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CreditController {

    @GetMapping("/credit")
    public String credit() {
        return "credit";
    }

}
