package com.example.forummanagementsystem.controllers.mvc;

import com.example.forummanagementsystem.models.Comment;
import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.services.PostService;
import com.example.forummanagementsystem.services.UserService;
import com.example.forummanagementsystem.services.mappers.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        List<Comment> listComments = postService.getCommentsByPostId(id);
        if (!listComments.isEmpty()) {
            model.addAttribute("comments", listComments);
        }
        Optional<Post> optionalPost = Optional.ofNullable(this.postService.getById(id));
        if (optionalPost.isPresent()) {

            Post post = optionalPost.get();
            model.addAttribute("post", post);

            return "post";
        } else {
            return "error";
        }
    }
    @GetMapping("new")
    public String createNewPost(Model model) {
                                                                   //TODO Authentication to be implemented
        User randomUser = userService.get(3L);
            Post post = new Post();
            post.setUser(randomUser);
            model.addAttribute("post", post);
            return "post_new";
        }

    @PostMapping("new")
    public String createNewPost(@ModelAttribute Post post) {
        // Get the user object based on the user id string        //TODO Authentication to be implemented
        User user = userService.get((post.getUser()).getId());
        post.setUser(user);

        postService.create(post);
        return "redirect:/posts/" + post.getPostId();
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
