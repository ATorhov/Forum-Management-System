package com.example.forummanagementsystem.services;

import com.example.forummanagementsystem.models.Comment;


import java.util.List;

public interface CommentService {




    List<Comment> getAll();

    Comment getById(int id);

    void create(Comment comment);

    void update(Comment comment);

    void delete(int id);
}
