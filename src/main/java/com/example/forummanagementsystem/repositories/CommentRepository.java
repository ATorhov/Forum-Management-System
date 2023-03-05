package com.example.forummanagementsystem.repositories;

import com.example.forummanagementsystem.models.Comment;
import com.example.forummanagementsystem.models.User;


import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    List<Comment> getAll();

    List<Comment> getCommentsByUserId(Long id);

    Comment getById(int id);

    void create(Comment comment);

    void update(Comment comment);

  //      void update(Comment comment, User user);

    void delete(int id);

//    List<Comment> filter(Optional<String> content,Optional<Integer> postId, Optional<Integer> userId, Optional<String> sort);

//    List<Comment> filter(Optional<String> content,
//                         Optional<Integer> commentId,
//                         Optional<Integer> postId,
//                         Optional<Integer> userId,
//                         Optional<String> sort);

    List<Comment> filter(Optional<String> content,

                         Optional<Integer> postId,
                         Optional<Integer> userId,
                         Optional<String> sort);

    List<Comment> getAll(Optional<String> search);


}