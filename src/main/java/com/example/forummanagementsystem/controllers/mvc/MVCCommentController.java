package com.example.forummanagementsystem.controllers.mvc;

import com.example.forummanagementsystem.models.Comment;
import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.services.CommentService;
import com.example.forummanagementsystem.services.PostService;
import com.example.forummanagementsystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/posts/")

public class MVCCommentController {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @GetMapping("{postId}/comments/new")
    public String createNewComment(Model model, @PathVariable Long postId) {
        Post post = postService.getById(postId);
        User user = postService.getById(postId).getUser();
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        model.addAttribute("comment", comment);
        return "comment_new";
    }

    @PostMapping("{postId}/comments/new")
    public String createNewComment(@Valid @ModelAttribute("comment") Comment comment, @PathVariable Long postId, BindingResult errors) {
        if (errors.hasErrors()) {
            return "comment_new";
        }

        Post post = postService.getById(postId);
        User user = userService.get((post.getUser()).getId());
        commentService.create(comment, user, post, postId);

        return "redirect:/posts/" + postId;
    }


    @PostMapping("{postId}/comments/{commentId}/edit")

    public String updateComment(@PathVariable Long postId, Comment comment, @PathVariable int commentId, Model model) {

        Optional<Comment> optionalComment = Optional.ofNullable(commentService.getById(commentId));
        Post post = postService.getById(postId);
        if (optionalComment.isPresent() && post != null) {
            Comment commentToUpdate = optionalComment.get();
            commentToUpdate.setContent(comment.getContent());

            commentService.update(commentToUpdate, userService.get(commentToUpdate.getUser().getId()));
        }
        return "redirect:/posts/" + postId;
    }

    @GetMapping("{postId}/comments/{commentId}/edit")
    public String getCommentForEdit(@PathVariable Long postId, @PathVariable int commentId, Model model) {
        Optional<Comment> optionalComment = Optional.ofNullable(commentService.getById(commentId));
        Post post = postService.getById(postId);
        User user = post.getUser();
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            comment.setUser(user);
            model.addAttribute("comment", comment);
            return "comment_edit";
        } else {
            return "error";
        }
    }

    @GetMapping("{postId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable Long postId, @PathVariable int commentId) {
        Optional<Post> optionalPost = Optional.ofNullable(postService.getById(postId));
        if (optionalPost.isPresent()) {
            Comment comment = commentService.getById(commentId);
            User user = comment.getUser();
            commentService.delete(commentId, user);

            return "redirect:/posts/" + postId;
        } else {
            return "error";
        }
    }
}
