package com.example.forummanagementsystem.controllers;

import com.example.forummanagementsystem.exceptions.AuthorizationException;
import com.example.forummanagementsystem.helpers.AuthenticationHelper;
import com.example.forummanagementsystem.models.*;

import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.services.PostService;
import com.example.forummanagementsystem.services.mappers.PostMapper;


import com.example.forummanagementsystem.services.CommentService;
import com.example.forummanagementsystem.services.mappers.CommentMapper;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


import com.example.forummanagementsystem.exceptions.EntityDuplicateException;
import com.example.forummanagementsystem.exceptions.EntityNotFoundException;

import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("api/comments")


public class CommentController {

    private final CommentService commentService;

    private final PostService postService;

    private final CommentMapper modelMapper;

    private final PostMapper modelMapperPost;

    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public CommentController(CommentService commentService, PostService postService, CommentMapper modelMapper, PostMapper modelMapperPost, AuthenticationHelper authenticationHelper) {
        this.commentService = commentService;
        this.postService = postService;
        this.modelMapper = modelMapper;
        this.modelMapperPost = modelMapperPost;
        this.authenticationHelper = authenticationHelper;

    }

    @GetMapping
    public List<Comment> getAll(@RequestParam(required = false) Optional<String> search) {
        return commentService.getAll(search);
    }

    @GetMapping("/filter")
    public List<Comment> filter(
            @RequestParam(required = false) Optional<String> content,
            @RequestParam(required = false) Optional<Integer> comment_id,
            @RequestParam(required = false) Optional<Integer> post_id,
            @RequestParam(required = false) Optional<Integer> user_id,
            @RequestParam(required = false) Optional<String> sort
    ) {
        try {
            return commentService.filter(content, comment_id, post_id, user_id, sort);
        } catch (UnsupportedOperationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Comment getById(@PathVariable int id) {
        try {
            return commentService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/user/{id}")
    public List<Comment> getCommentsByUserId(@PathVariable Long id) {
        return commentService.getCommentsByUserId(id);
    }

    @PostMapping
    public Comment create(
            @RequestHeader HttpHeaders headers,
            @RequestParam Long postId,
            @Valid @RequestBody CommentDto commentDto
    ) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Post post = postService.getById(postId);

            Comment comment = modelMapper.dtoToObjectCommentForCreate(commentDto, user, post);
            commentService.create(comment, user, post, postId);
            return comment;

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Comment update(@RequestHeader HttpHeaders headers, @PathVariable int id,
                          @Valid @RequestBody CommentDto commentDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Comment comment = modelMapper.fromDto(id, commentDto);
            Comment commentToUpdate = commentService.getById(id);
            Comment newComment = modelMapper.dtoToObjectComment(commentDto, commentToUpdate.getUser(), commentToUpdate.getPost());
            authenticationHelper.checkPermissions(commentService.getById(id).getUser().getId(), user);
            newComment.setCommentId(id);
            commentService.update(comment, commentToUpdate.getUser());
            return comment;

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            authenticationHelper.checkPermissions(commentService.getById(id).getUser().getId(), user);
            commentService.delete(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
