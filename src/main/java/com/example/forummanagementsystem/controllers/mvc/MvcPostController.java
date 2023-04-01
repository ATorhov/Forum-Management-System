package com.example.forummanagementsystem.controllers.mvc;

import com.example.forummanagementsystem.exceptions.AuthorizationException;
import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.exceptions.UnauthorizerOperationException;
import com.example.forummanagementsystem.helpers.AuthenticationHelper;
import com.example.forummanagementsystem.models.*;
import com.example.forummanagementsystem.models.dtos.PostDto;
import com.example.forummanagementsystem.models.dtos.PostDtoEdit;
import com.example.forummanagementsystem.services.PostService;
import com.example.forummanagementsystem.services.UserService;
import com.example.forummanagementsystem.services.mappers.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/posts/")
public class MvcPostController {
    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationHelper authenticationHelper;

    @Autowired
    private PostMapper postMapper;

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

    @GetMapping("/all")
    public String getUsers(Model model, HttpSession session){
        User user = authenticationHelper.tryGetUser(session);
        try{
            model.addAttribute("posts", postService.getAll(user));
        } catch (UnauthorizerOperationException e){
            return "access_denied";
        }
        return "all-posts-page";
    }

    @GetMapping("{postId}")
    public String getPost(@PathVariable Long postId, Model model, HttpSession httpSession) {
        try {
            Post post = postService.getById(postId);
            model.addAttribute("post", post);
            List<Comment> comments = postService.getCommentsByPostId(postId);
            model.addAttribute("comments", comments);
            return "post";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }
    @GetMapping("new")
    public String createNewPost(Model model, HttpSession httpSession) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(httpSession);
        } catch (AuthorizationException e){
            return "redirect:/auth/login";
        }
        model.addAttribute("postDto", new PostDto());
        model.addAttribute("user", user);
        return "post_new";
        }

    @PostMapping("new")
    public String createNewPost(@Valid @ModelAttribute("postDto") PostDto postDto, BindingResult bindingResult,
                                Model model,
                                HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            if (bindingResult.hasErrors()) {
                return "post_new";
            }
            Post post = postMapper.createDtoToObject(postDto, user);
            postService.create(post);
            return "redirect:/posts/" + post.getPostId();
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @GetMapping("{postId}/edit")
    public String updatePost(@PathVariable("postId") Long postId, Model model, HttpSession session) {
        try {
            User currentUser = authenticationHelper.tryGetUser(session);
            authenticationHelper.checkAccessPermissions(postService.getById(postId).getUser().getId(), currentUser);

            Post post = postService.getById(postId);
            PostDtoEdit postDto = postMapper.updateObjectToDto(post);
            model.addAttribute("postId", postId);
            model.addAttribute("post", postDto);
            return "post_edit";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        } catch (UnauthorizerOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "access_denied";
        }
    }

    @PostMapping("{postId}/edit")
    public String updatePost(@PathVariable("postId") Long postId, @Valid @ModelAttribute("postDto") PostDtoEdit postDtoEdit,
                             BindingResult bindingResult,
                             Model model,
                             HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            authenticationHelper.checkAccessPermissions(postService.getById(postId).getUser().getId(), user);
            if (bindingResult.hasErrors()) {
                return "post_edit";
            }
            Post post = postService.getById(postId);
            post = postMapper.fromDtoEdit(postDtoEdit, postId);
            postService.update(post);
            return "redirect:/posts/" + post.getPostId();
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        } catch (UnauthorizerOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "access_denied";
        }
    }


    @GetMapping("{postId}/delete")
    public String deletePost(@PathVariable("postId") Long postId, Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            authenticationHelper.checkAccessPermissions(postService.getById(postId).getUser().getId(), user);
            postService.delete(postId);
            return "redirect:/home";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        } catch (UnauthorizerOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "access_denied";
        }
    }

    @GetMapping("{postId}/opinion")
    public String addOpinion(HttpSession session, @PathVariable Long postId, @RequestParam Long opinion) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthorizationException e){
            throw new AuthorizationException("User is not authenticated");
        }
        Post post = postService.getById(postId);
        postService.addOpinion(user, post, opinion);
        Map<String, Long> result = new HashMap<>();
        result.put("likes", postService.getLikes(post));
        result.put("dislikes", postService.getDislikes(post));
        post.setRealRating();
        postService.update(post);
        return "redirect:/posts/"+postId;
    }
}
