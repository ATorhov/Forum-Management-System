package com.example.forummanagementsystem.controllers;


import com.example.forummanagementsystem.models.Comment;
import com.example.forummanagementsystem.models.CommentDto;

import com.example.forummanagementsystem.services.CommentService;
import com.example.forummanagementsystem.services.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import javax.validation.Valid;
import java.util.List;


import com.example.forummanagementsystem.exceptions.EntityDuplicateException;
import com.example.forummanagementsystem.exceptions.EntityNotFoundException;

import org.springframework.http.HttpStatus;



@RestController
@RequestMapping("api/comments")


public class CommentController {

    private final CommentService commentService;

    private final ModelMapper modelMapper;

    @Autowired
    public CommentController(CommentService commentService, ModelMapper modelMapper) {
        this.commentService = commentService;
        this.modelMapper = modelMapper;

    }

    @GetMapping
    public List<Comment> getAll() {
        return commentService.getAll();
    }

    @GetMapping("/{id}")
    public Comment getById(@PathVariable int id) {
        try {
            return commentService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public Comment create(@Valid @RequestBody CommentDto commentDto) {
        try {
            Comment comment = modelMapper.fromDtoComment(commentDto);
            commentService.create(comment);
            return comment;
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Comment update(@PathVariable int id, @Valid @RequestBody CommentDto commentDto) {
        try {
            Comment comment = modelMapper.fromDtoComment(commentDto,id);
            commentService.update(comment);
            return comment;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        try {
            commentService.delete(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
