package com.example.forummanagementsystem.controllers.mvc;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping
public class ContactMVCController {
    @ModelAttribute("isAuthenticated")
    public boolean isAuthenticated(HttpSession httpSession) {
        return httpSession.getAttribute("currentUser") != null;
    }

    @PostMapping("/contact")
    public String contact(Model model) {
        return "contact";
    }

    @GetMapping("/contact")
    public String contact(HttpSession httpSession) {

        return "contact";
    }

    @PostMapping("posts/contact")
    public String contactFromPosts(Model model) {
        return "redirect:/contact";
    }

    @GetMapping("posts/contact")
    public String contactFromPosts(HttpSession httpSession) {

        return "redirect:/contact";
    }
}
