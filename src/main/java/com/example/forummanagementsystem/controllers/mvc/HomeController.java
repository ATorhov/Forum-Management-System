package com.example.forummanagementsystem.controllers.mvc;

import com.example.forummanagementsystem.exceptions.AuthorizationException;
import com.example.forummanagementsystem.helpers.AuthenticationHelper;
import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.dtos.PostFilterDto;
import com.example.forummanagementsystem.models.PostFilterOptions;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.services.PostService;
import com.example.forummanagementsystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private PostService postService;

    private final UserService userService;

    private final AuthenticationHelper authenticationHelper;

    public HomeController(UserService userService, AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("user")
    public User getUser(HttpSession httpSession) {
        User user = null;
        try {
            user =  authenticationHelper.tryGetUser(httpSession);
        } catch (AuthorizationException e){
            
        }
        return user;
    }

    @ModelAttribute("isAuthenticated")
    public boolean isAuthenticated(HttpSession httpSession) {
        return httpSession.getAttribute("currentUser") != null;
    }


    @ModelAttribute("isAdmin")
    public boolean isAdmin(HttpSession httpSession) {
        if (populateIsAuthenticated(httpSession)) {
            return userService.get(httpSession.getAttribute("currentUser").toString()).isAdmin();
        } else {
            return false;
        }
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @PostMapping("/home")
    public String home(Model model) {
        List<Post> posts = postService.getAll();
        model.addAttribute("posts", posts);
        return "home";
    }

    @GetMapping("/home")
    public String get(@ModelAttribute("filterOptions") PostFilterDto filterDto, Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetUser(session);
        } catch (AuthorizationException e){
            return "redirect:/auth/login";
        }
        PostFilterOptions filterOptions = new PostFilterOptions(
                filterDto.getTitle(),
                filterDto.getContent(),
                filterDto.getRating(),
                filterDto.getCreateDateTime(),
                filterDto.getUpdateDateTime(),
                filterDto.getSortBy(),
                filterDto.getSortOrder()
        );
        model.addAttribute("posts", postService.get(filterOptions));
        model.addAttribute("filterOptions", filterDto);
        return "home";
    }

    @GetMapping("/home/index")
    public String getIndex(@ModelAttribute("filterOptions") PostFilterDto filterDto, Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetUser(session);
            List<Post> mostCommentedPosts = postService.findTenMostCommentedPosts();
            List<Post> recentlyCreatedPosts = postService.findTenMostRecentCreatedPosts();
            List<Post> topRatedPosts = postService.findTenMostRatedPosts();

            model.addAttribute("posts", topRatedPosts);
            model.addAttribute("mostCommentedPosts", mostCommentedPosts);
            model.addAttribute("recentlyCreatedPosts", recentlyCreatedPosts);
        } catch (AuthorizationException e){
            return "redirect:/auth/login";
        }
        return "index";
    }

}