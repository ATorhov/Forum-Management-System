package com.example.forummanagementsystem.controllers.mvc;

import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class GuestController {

    @Autowired
    private PostService service;

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<Post> posts = service.getAll();
        posts.sort((p1, p2) -> Long.compare(p2.getRating(), p1.getRating()));
        List<Post> topFivePosts = posts.subList(0, Math.min(posts.size(), 5));
        model.addAttribute("posts", topFivePosts);
        return "index";
    }
}