package com.example.forummanagementsystem.repositories;

import com.example.forummanagementsystem.models.Post;

import java.util.List;

public interface PostRepository {

    List<Post> getAll();
    Post getById(int id);

    Post getByTitle(String name);

    void create(Post post);
    void update(Post post);
    void delete(int id);


}
