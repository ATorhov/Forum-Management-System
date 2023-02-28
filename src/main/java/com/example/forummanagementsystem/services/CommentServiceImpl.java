package com.example.forummanagementsystem.services;


import com.example.forummanagementsystem.exceptions.CommentNotFoundException;
import com.example.forummanagementsystem.exceptions.EntityDuplicateEntitiesException;
import com.example.forummanagementsystem.models.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.example.forummanagementsystem.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;


@Component
public class CommentServiceImpl implements CommentService {

    private final CommentRepository repository;


    private final List<Comment> comments;


    @Autowired
    public CommentServiceImpl(CommentRepository repository) {
        this.repository = repository;
        comments = repository.getAll();
    }

    @Override
    public List<Comment> getAll() {
        return comments;
    }

    @Override
    public Comment getById(int id) {
        return comments.get(id);
    }

    @Override
    public void create(Comment comment) {
        boolean duplicateExists = true;

        try {
            repository.getById(comment.getCommentId());
        } catch (CommentNotFoundException e) {
            duplicateExists = false;
        }
        if (duplicateExists) {
            throw new EntityDuplicateEntitiesException(comment.getCommentId(), comment.getPostId());
        }
        repository.create(comment);

    }


    @Override
    public void update(Comment comment) {

        boolean duplicateExists = true;
        try {
            Comment existingComment = repository.getById(comment.getCommentId());
            if (Objects.equals(existingComment.getCommentId(), comment.getCommentId())) {
                duplicateExists = false;
            }
        } catch (CommentNotFoundException e) {
            duplicateExists = false;
        }
        if (duplicateExists) {
            throw new EntityDuplicateEntitiesException(comment.getCommentId(), comment.getPostId());
        }
        repository.update(comment);
    }

    @Override
    public void delete(int id) {
        repository.delete(id);
    }




}


