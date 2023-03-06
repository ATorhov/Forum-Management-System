package com.example.forummanagementsystem.services;

import com.example.forummanagementsystem.models.Comment;
import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.User;


import java.util.List;
import java.util.Optional;

public interface CommentService {


    Comment getById(int id);


    List<Comment> getCommentsByUserId(Long id);

//    List<Comment> getCommentsByUsername(String username);


    void create(Comment comment, User user, Post post, Long id);

    void update(Comment comment, User user);


    void delete(int id, User user);

    //  List<Comment> filter(Optional<String> content, Optional<Integer> commentId, Optional<Integer> postId, Optional<Integer> userId, Optional<String> sort);

    List<Comment> filter(Optional<String> content, Optional<Integer> commentId, Optional<Integer> postId,
                         Optional<Integer> userId, Optional<String> sort);

    List<Comment> getAll(Optional<String> search);
}
