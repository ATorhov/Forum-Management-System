package com.example.forummanagementsystem.controllers.mvc;

import com.example.forummanagementsystem.exceptions.AuthorizationException;
import com.example.forummanagementsystem.helpers.AuthenticationHelper;
import com.example.forummanagementsystem.models.*;
import com.example.forummanagementsystem.services.CommentService;
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

public class MVCFilteringCommentController {


    @Autowired
    private CommentService commentService;


    private final PostService postService;

    private final UserService userService;

    private final AuthenticationHelper authenticationHelper;


    public MVCFilteringCommentController(PostService postService, UserService userService, AuthenticationHelper authenticationHelper) {
        this.postService = postService;
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
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


    @PostMapping("FilterComments")
    public String showAllCommentsOfAPost(Model model) {
        List<Comment> comments = commentService.getAll();
        model.addAttribute("comments", comments);
        return "FilterComments";
    }



    @GetMapping("FilterComments")
    public String showAllCommentsOfAPost(@ModelAttribute("filterCommentOptions") CommentFilterDto filterCommentDto, Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        CommentFilterOptions filterCommentOptions = new CommentFilterOptions(
                filterCommentDto.getContent(),
                filterCommentDto.getCommentId(),
                filterCommentDto.getSortBy(),
                filterCommentDto.getSortOrder(),
                filterCommentDto.getPost(),
                filterCommentDto.getUser(),
                filterCommentDto.getPostId(),
                filterCommentDto.getUserId()
        );

        model.addAttribute("comments", commentService.filter(filterCommentOptions));
        model.addAttribute("filterCommentOptions", filterCommentDto);

        return "FilterComments";
    }
}

