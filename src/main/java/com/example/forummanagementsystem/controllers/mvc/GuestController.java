package com.example.forummanagementsystem.controllers.mvc;

import com.example.forummanagementsystem.helpers.AuthenticationHelper;
import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

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

        List<Post> mostCommentedPosts = service.findTenMostCommentedPosts();
        List<Post> recentlyCreatedPosts = service.findTenMostRecentCreatedPosts();
        List<Post> topRatedPosts = service.findTenMostRatedPosts();

        model.addAttribute("posts", topRatedPosts);
        model.addAttribute("mostCommentedPosts", mostCommentedPosts);
        model.addAttribute("recentlyCreatedPosts", recentlyCreatedPosts);

        return "index";
    }
}