package com.example.forummanagementsystem.controllers.mvc;

import com.example.forummanagementsystem.exceptions.AuthorizationException;
import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.exceptions.UnauthorizerOperationException;
import com.example.forummanagementsystem.helpers.AuthenticationHelper;
import com.example.forummanagementsystem.models.*;
import com.example.forummanagementsystem.models.dtos.CommentDto;
import com.example.forummanagementsystem.services.CommentService;
import com.example.forummanagementsystem.services.PostService;
import com.example.forummanagementsystem.services.UserService;
import com.example.forummanagementsystem.services.mappers.CommentMapper;
import com.example.forummanagementsystem.services.mappers.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/posts/")

public class MVCCommentController {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private AuthenticationHelper authenticationHelper;

    @Autowired
    private UserService userService;

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
        if (isAuthenticated(httpSession)) {
            return userService.get(httpSession.getAttribute("currentUser").toString()).isAdmin();
        } else {
            return false;
        }
    }

    @GetMapping("{postId}/comments/new")
    public String createNewComment(Model model, @PathVariable Long postId, HttpSession httpSession) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(httpSession);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        model.addAttribute("commentDto", new CommentDto());
        model.addAttribute("user", user);
        return "comment_new";
    }

    @PostMapping("{postId}/comments/new")
    public String createNewComment(@Valid @ModelAttribute("commentDto") CommentDto commentDto, @PathVariable Long postId, BindingResult bindingResult, Model model, HttpSession httpSession) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(httpSession);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        if (bindingResult.hasErrors()) {
            return "comment_new";
        }
        try {
            Comment comment = commentMapper.createDtoToObject(commentDto, user);
            commentService.create(comment, user, postService.getById(postId), postId);
            return "redirect:/posts/" + postId;
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @GetMapping("{postId}/comments/{commentId}/edit")
    public String updateComment(@PathVariable Long postId, @PathVariable int commentId, Model model, HttpSession httpSession) {

        try {
            authenticationHelper.tryGetUser(httpSession);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        User currentUser = authenticationHelper.tryGetUser(httpSession);
        try {
            authenticationHelper.checkAccessPermissions(commentService.getById(commentId).getUser().getId(), currentUser);
        } catch (UnauthorizerOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "access_denied";
        }
        Comment comment = commentService.getById(commentId);
        CommentDto commentDto = commentMapper.createObjectToDto(comment);
        model.addAttribute("commentDto", commentDto);
        return "comment_edit";
    }

    @PostMapping("{postId}/comments/{commentId}/edit")

    public String getCommentForEdit(@PathVariable Long postId, @Valid @ModelAttribute("commentDto") CommentDto commentDto, @PathVariable int commentId, Model model, BindingResult bindingResult, HttpSession httpSession) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(httpSession);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        try {
            authenticationHelper.checkAccessPermissions(commentService.getById(commentId).getUser().getId(), user);
        } catch (UnauthorizerOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "access_denied";
        }
        if (bindingResult.hasErrors()) {
            return "comment_edit";
        }
        try {
            Comment comment = commentMapper.createDtoToObject(commentDto, user, commentId);
            commentService.update(comment, user);
            return "redirect:/posts/" + postId;
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @GetMapping("{postId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable Long postId, @PathVariable int commentId, Model model, HttpSession httpSession) {
        try {
            authenticationHelper.tryGetUser(httpSession);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        User user = authenticationHelper.tryGetUser(httpSession);
        try {
            authenticationHelper.checkAccessPermissions(commentService.getById(commentId).getUser().getId(), user);
        } catch (UnauthorizerOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "access_denied";
        }
        try {
            commentService.delete(commentId, user);
            return "redirect:/posts/" + postId;
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }
}
