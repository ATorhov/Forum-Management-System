package com.example.forummanagementsystem.repositories;

import com.example.forummanagementsystem.models.Comment;
import com.example.forummanagementsystem.models.CommentFilterOptions;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    List<Comment> getAll();

    List<Comment> getCommentsByUserId(Long id);

    Comment getById(int id);

    void create(Comment comment);

    void update(Comment comment);

    void delete(int id);

    List<Comment> get(CommentFilterOptions commentFilterOptions);

    List<Comment> filter(Optional<String> content,
                         Optional<Integer> commentId,

                         Optional<Integer> postId,
                         Optional<Integer> userId,
                         Optional<String> sort);

    List<Comment> getCommentsFilterCommentOptions(CommentFilterOptions filterCommentOptions);

    List<Comment> getAll(Optional<String> search);

    int getCommentsCount();
}