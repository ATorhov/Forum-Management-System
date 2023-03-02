package com.example.forummanagementsystem.repositories;

import com.example.forummanagementsystem.models.Comment;
import com.example.forummanagementsystem.models.User;


import java.util.List;

public interface CommentRepository {


    List<Comment> getAll();

    Comment getById(int id);

    void create(Comment comment);

    void update(Comment comment);

    void update(Comment comment, User user);

    void delete(int id);
}