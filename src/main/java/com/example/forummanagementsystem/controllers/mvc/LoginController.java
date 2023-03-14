package com.example.forummanagementsystem.controllers.mvc;

import com.example.forummanagementsystem.helpers.AuthenticationHelper;
import com.example.forummanagementsystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationHelper authenticationHelper;

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }
}
