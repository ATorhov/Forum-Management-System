package com.example.forummanagementsystem.controllers;

import com.example.forummanagementsystem.exceptions.*;
import com.example.forummanagementsystem.models.Comment;
import com.example.forummanagementsystem.models.CommentDto;
import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.PostDto;
import com.example.forummanagementsystem.services.CommentService;
import com.example.forummanagementsystem.services.ModelMapper;
import com.example.forummanagementsystem.services.ModelMapperComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("api/comments")
public class CommentController {

    private final CommentService commentService;
    private final ModelMapperComment modelMapperComment;



    @Autowired
    public CommentController(CommentService commentService, ModelMapperComment modelMapperComment) {
        this.commentService = commentService;
        this.modelMapperComment = modelMapperComment;
    }

    @GetMapping
    public List<Comment> getAll() {
        return commentService.getAll();
    }

    @GetMapping("/{id}")

    public Comment getById(@PathVariable int id) {
        try {
            return commentService.getById(id);
        } catch (CommentNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public Comment create(@Valid @RequestBody CommentDto commentDto) {
        try {
            Comment comment = modelMapperComment.fromDtoComment(commentDto);
            commentService.create(comment);
            return comment;
        } catch (CommentNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }


    @PutMapping("/{id}")
    public Comment update(@PathVariable int id, @Valid @RequestBody CommentDto commentDto) {
        try {
            Comment comment = modelMapperComment.fromDtoComment(commentDto, id);
            commentService.update(comment);
            return comment;
        } catch (CommentNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        try {
            commentService.delete(id);
        } catch (CommentNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
