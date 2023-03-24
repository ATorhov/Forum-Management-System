package com.example.forummanagementsystem.controllers.mvc;

import com.example.forummanagementsystem.exceptions.AuthorizationException;
import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.helpers.AuthenticationHelper;
import com.example.forummanagementsystem.models.*;
import com.example.forummanagementsystem.services.PostService;
import com.example.forummanagementsystem.services.UserService;
import com.example.forummanagementsystem.services.mappers.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @GetMapping("{id}")
    public String getPost(@PathVariable Long id, Model model, HttpSession httpSession) {
        try {
            authenticationHelper.tryGetUser(httpSession);
        }catch (AuthorizationException e){
            return "redirect:/auth/login";
        }
        try {
            Post post = postService.getById(id);
            model.addAttribute("post", post);
//            System.out.println(post.getLikes());
//            System.out.println(post.getDislikes());
            List<Comment> comments = postService.getCommentsByPostId(id);
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
    public String createNewPost(@Valid @ModelAttribute("postDto") PostDto postDto, BindingResult bindingResult, Model model, HttpSession httpSession) {
       User user;
       try {
           user = authenticationHelper.tryGetUser(httpSession);
       } catch (AuthorizationException e){
           return "redirect:/auth/login";
       }
       if (bindingResult.hasErrors()) {
           return "post_new";
       }
       try {
           Post post = postMapper.createDtoToObject(postDto, user);
           postService.create(post);
           return "redirect:/posts/" + post.getPostId();
       } catch (EntityNotFoundException e){
           model.addAttribute("error", e.getMessage());
           return "error";
       }
    }

    @GetMapping("{id}/edit")
    public String updatePost(@PathVariable("id") Long id, Model model, HttpSession httpSession) {
        try {
            authenticationHelper.tryGetUser(httpSession);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        User currentUser = authenticationHelper.tryGetUser(httpSession);
        try {
            authenticationHelper.checkAccessPermissions(postService.getById(id).getUser().getId(), currentUser);
        } catch (ResponseStatusException e) {
            model.addAttribute("error", e.getMessage());
            return "access_denied";
        }
        try {
            Post post = postService.getById(id);
            PostDto postDto = postMapper.createObjectToDto(post);
            model.addAttribute("postId", id);
            model.addAttribute("post", postDto);
            return "post_edit";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @PostMapping("{id}/edit")
    public String getPostForEdit(@PathVariable("id") Long id, @Valid @ModelAttribute("postDto") PostDto postDto, BindingResult bindingResult, Model model, HttpSession httpSession) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(httpSession);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        try {
            authenticationHelper.checkAccessPermissions(postService.getById(id).getUser().getId(), user);
        } catch (ResponseStatusException e) {
            model.addAttribute("error", e.getMessage());
            return "access_denied";
        }
        if (bindingResult.hasErrors()){
            return "post_edit";
        }
        try {
            Post post = postMapper.createDtoToObject(postDto, user, id);
            post.setUpdateTime(LocalDateTime.now());
            postService.update(post);
            return "redirect:/posts/"+post.getPostId();
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }


    @GetMapping("{id}/delete")
    public String deletePost(@PathVariable Long id, Model model, HttpSession httpSession) {
        try {
            authenticationHelper.tryGetUser(httpSession);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        User user = authenticationHelper.tryGetUser(httpSession);
        try {
            authenticationHelper.checkAccessPermissions(postService.getById(id).getUser().getId(), user);
        } catch (ResponseStatusException e){
            model.addAttribute("error", e.getMessage());
            return "access_denied";
        }
        try {
            postService.delete(id);
            return "redirect:/home";
        } catch (EntityNotFoundException e){
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }
    @GetMapping("{id}/opinion")
    public String addOpinion(HttpSession session, @PathVariable Long id, @RequestParam Long opinion) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthorizationException e){
            throw new AuthorizationException("User is not authenticated");
        }
        Post post = postService.getById(id);
        postService.addOpinion(user, post, opinion);
        Map<String, Long> result = new HashMap<>();
        result.put("likes", post.getLikes());
        result.put("dislikes", post.getDislikes());
        return "redirect:/posts/"+id;
    }
}
