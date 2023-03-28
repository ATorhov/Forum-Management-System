package com.example.forummanagementsystem.services;

import com.example.forummanagementsystem.exceptions.AuthorizationException;
import com.example.forummanagementsystem.exceptions.BlockedUserException;
import com.example.forummanagementsystem.models.Comment;

import java.util.List;
import java.util.Optional;

import com.example.forummanagementsystem.models.CommentFilterOptions;
import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.forummanagementsystem.repositories.CommentRepository;


@Service
public class CommentServiceImpl implements CommentService {

    private static final String MODIFY_COMMENT_ERROR_MESSAGE = "Only admin or comment creator can modify a comment.";
    public static final String BLOCKED_USER_ERROR_FOR_COMMENTS = "Blocked users are not allowed to create comments.";

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


    @Override
    public void create(Comment comment, User user, Post post, Long id) {
        comment.setUser(user);
        comment.setPost(post);
        if (comment.getUser().isBlocked()) {
            throw new BlockedUserException(BLOCKED_USER_ERROR_FOR_COMMENTS);
        }
        repository.create(comment);
    }

    @Override
    public void update(Comment comment, User user) {
        checkModifyPermissions(comment.getCommentId(), user);
        if (comment.getUser().isBlocked()) {
            throw new BlockedUserException(BLOCKED_USER_ERROR_FOR_COMMENTS);
        }
        repository.update(comment);
    }

    @Override
    public void delete(int id, User user) {
        checkModifyPermissions(id, user);
        if (getById(id).getUser().isBlocked()) {
            throw new BlockedUserException(BLOCKED_USER_ERROR_FOR_COMMENTS);
        }
        repository.delete(id);
    }

    @Override
    public List<Comment> filter(Optional<String> content, Optional<Integer> commentId, Optional<Integer> postId,
                                Optional<Integer> userId, Optional<String> sort) {
        return repository.filter(content, commentId, postId, userId, sort);
    }

    @Override
    public List<Comment> getAll(Optional<String> search) {
        return repository.getAll(search);
    }

    public List<Comment> getAll() {
        return repository.getAll();
    }

    @Override
    public List<Comment> getAll(User user) {
        if (!user.isAdmin()) {
            throw new AuthorizationException("Only admin can see all the users!");
        }
        return repository.getAll();
    }

    @Override
    public List<Comment> filter(CommentFilterOptions filterCommentOptions) {
        return repository.getCommentsFilterCommentOptions(filterCommentOptions);
    }

    private void checkModifyPermissions(int commentId, User user) {
        Comment comment = repository.getById(commentId);
        if (!(user.isAdmin() || comment.getUser().equals(user))) {
            throw new AuthorizationException(MODIFY_COMMENT_ERROR_MESSAGE);
        }
    }
}


