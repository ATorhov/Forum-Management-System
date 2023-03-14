package com.example.forummanagementsystem.controllers.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LogoutController {

    @GetMapping("/logout")
    public String getLogoutPage(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/";
    }
}
