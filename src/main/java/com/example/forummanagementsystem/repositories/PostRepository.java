package com.example.forummanagementsystem.repositories;

import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.User;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    List<Post> getAll();
    Post getById(Long id);

    Post getByTitle(String name);

    void create(Post post);
    void update(Post post);
    void delete(Long id);

    List<Post> getPostsByUserId(Long userId);

    List<Post> filter(Optional<String> title, Optional<String> content, Optional<Integer> rating, Optional<String> sort);

    void update(Post post, User user);

    List<Post> search(Optional<String> all);
}
