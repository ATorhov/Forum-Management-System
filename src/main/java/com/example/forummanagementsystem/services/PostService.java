package com.example.forummanagementsystem.services;

import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.User;

import java.util.List;

public interface PostService {

    List<Post> getAll();
    Post getById(Long id);

    Post getByTitle(String name);

    void create(Post post);
    void update(Post post);
    void delete(Long id);

    void update(Post post, User user);

}
