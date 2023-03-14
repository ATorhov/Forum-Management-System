package com.example.forummanagementsystem.controllers.mvc;

import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegisterController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String getRegisterPage(Model model){
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register")
    public String registerNewUser(@ModelAttribute User user) {
        userService.create(user);
        return "redirect:/home";
    }

}