package com.example.forummanagementsystem.services;

import com.example.forummanagementsystem.exceptions.AuthorizationException;
import com.example.forummanagementsystem.exceptions.EntityDuplicateException;
import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.models.Comment;

import java.util.List;
import java.util.Optional;

import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.forummanagementsystem.repositories.CommentRepository;


@Service
public class CommentServiceImpl implements CommentService {

    private static final String MODIFY_COMMENT_ERROR_MESSAGE = "Only admin or comment creator can modify a comment.";

    private final CommentRepository repository;

    @Autowired
    public CommentServiceImpl(CommentRepository repository) {
        this.repository = repository;
    }


    @Override
    public Comment getById(int id) {
        return repository.getById(id);
    }

    @Override
    public List<Comment> getCommentsByUserId(Long id) {
        return repository.getCommentsByUserId(id);
    }

//    @Override
//    public List<Comment> getCommentsByUsername(String username) {
//        return repository.getCommentsByUsername(username);
//    }


    @Override
    public void create(Comment comment, User user, Post post, Long id) {
        boolean duplicateExists = false;
        try {
            repository.create(comment);
        } catch (EntityNotFoundException e) {
            duplicateExists = true;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("Comment", "id", comment.getContent());
        }
        comment.setUser(user);
        comment.setPost(post);
    }

    @Override
    public void update(Comment comment, User user) {
        checkModifyPermissions(comment.getCommentId(), user);

        boolean duplicateExists = true;
        try {
            Comment existingComment = repository.getById(comment.getCommentId());
            if (existingComment.getCommentId() == comment.getCommentId()) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
        if (duplicateExists) {
            throw new EntityDuplicateException("Comment", "id", comment.getContent());
        }
        repository.update(comment);
    }

    @Override
    public void delete(int id, User user) {
        checkModifyPermissions(id, user);
        repository.delete(id);
    }



    @Override
    public List<Comment> filter(Optional<String> content, Optional<Integer> commentId,Optional<Integer> postId,
                                Optional<Integer> userId, Optional<String> sort) {
        return repository.filter(content, commentId, postId, userId, sort);
    }

    @Override
    public List<Comment> getAll(Optional<String> search) {
        return repository.getAll(search);
    }

    private void checkModifyPermissions(int commentId, User user) {
        Comment comment = repository.getById(commentId);
        if (!(user.isAdmin() || comment.getUser().equals(user))) {
            throw new AuthorizationException(MODIFY_COMMENT_ERROR_MESSAGE);
        }
    }
}


