package com.example.forummanagementsystem.services;

import com.example.forummanagementsystem.exceptions.EntityDuplicateException;
import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.models.Comment;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.example.forummanagementsystem.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.forummanagementsystem.repositories.CommentRepository;


@Service
public class CommentServiceImpl implements CommentService {


    private final CommentRepository repository;

    @Autowired
    public CommentServiceImpl(CommentRepository repository) {
        this.repository = repository;
    }
//
//    @Override
//    public List<Comment> getAll() {
//        return repository.getAll();
//    }

    @Override
    public Comment getById(int id) {
        return repository.getById(id);
    }


    @Override
    public void create(Comment comment) {

        repository.create(comment);
    }


    @Override
    public void update(Comment comment) {
        repository.update(comment);
    }

//    @Override
//    public void update(Comment comment) {
//        boolean duplicateExists = true;
//        try {
//            Comment existingComment = repository.getById(comment.getCommentId());
//            if (Objects.equals(existingComment.getCommentId(), comment.getCommentId())) {
//                duplicateExists = false;
//            }
//        } catch (EntityNotFoundException e) {
//            duplicateExists = false;
//        }
//        if (duplicateExists) {
//            throw new EntityDuplicateException("Comment", "comment", comment.getCommentId().toString());
//        }
//        repository.update(comment);
//    }


    @Override
    public void update(Comment comment, User user) {
        repository.update(comment);
    }

    @Override
    public void delete(int id) {
        repository.delete(id);
    }

    @Override
    public List<Comment> filter(Optional<String> content, Optional<Integer> commentId, Optional<Integer> postId, Optional<Integer> userId, Optional<String> sort) {
        return repository.filter(content, commentId, postId, userId, sort);
    }

    @Override
    public List<Comment> getAll(Optional<String> search) {
        return repository.getAll(search);
    }
}


