package com.example.forummanagementsystem.controllers.mvc;

import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.services.PostService;
import com.example.forummanagementsystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/posts/")
public class MvcPostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @GetMapping("{id}")
    public String getPost(@PathVariable Long id, Model model) {
        Optional<Post> optionalPost = Optional.ofNullable(this.postService.getById(id));
        if (optionalPost.isPresent()) {

            Post post = optionalPost.get();
            model.addAttribute("post", post);

            return "post";
        } else {
            return "error";
        }
    }

    @PostMapping("{id}/edit")
    public String updatePost(@PathVariable Long id, Post post, BindingResult result, Model model) {
        Optional<Post> optionalPost = Optional.ofNullable(postService.getById(id));
        if (optionalPost.isPresent()) {

            Post existingPost = optionalPost.get();
            existingPost.setTitle(post.getTitle());
            existingPost.setContent(post.getContent());

            postService.update(existingPost);
        }
        return "redirect:/posts/{id}";
    }

    @GetMapping("{id}/edit")
    public String getPostForEdit(@PathVariable Long id, Model model) {
        Optional<Post> optionalPost = Optional.ofNullable(postService.getById(id));
        if (optionalPost.isPresent()) {

            Post post = optionalPost.get();
            model.addAttribute("post", post);

            return "post_edit";
        } else {
            return "error";
        }
    }

    @GetMapping("{id}/delete")
    public String deletePost(@PathVariable Long id) {
        Optional<Post> optionalPost = Optional.ofNullable(postService.getById(id));
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();

            postService.delete(post.getPostId());
            return "redirect:/home";
        } else {
            return "error";
        }
    }

}
