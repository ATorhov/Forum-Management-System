package com.example.forummanagementsystem.controllers.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping
public class AboutMVCController {

    @PostMapping("/about")
    public String about(Model model) {
        return "about";
    }

    @GetMapping("/about")
    public String about(HttpSession httpSession) {

        return "about";
    }

    @PostMapping("posts/about")
    public String aboutFromPosts(Model model) {
        return "redirect:/about";
    }

    @GetMapping("posts/about")
    public String aboutFromPosts(HttpSession httpSession) {

        return "redirect:/about";
    }
}
