package com.shirobokov.creditpipelineusers.controller;


import com.shirobokov.creditpipelineusers.entity.User;
import com.shirobokov.creditpipelineusers.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;



    @GetMapping("/registration")
    public String registrationPage(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String registrationUser(@ModelAttribute("user") User user, Model model) {

        if (userService.findByPhone(user.getPhone()) != null) {

            System.out.println("Такой номер телефона уже зарегестрирован");

            user.setPhone(user.getPhone().substring(3));

            model.addAttribute("usernameError", "Такой номер телефона уже зарегестрирован");
            return "registration";
        }
        userService.registerUser(user);


        return "redirect:/";
    }

}
