package com.example.forummanagementsystem.services;

import com.example.forummanagementsystem.models.Comment;
import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.PostFilterOptions;
import com.example.forummanagementsystem.models.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostService {

    List<Post> getAll();

    List<Post> get(PostFilterOptions postFilterOptions);
    Post getById(Long id);

    Post getByTitle(String name);

    void create(Post post);
    void update(Post post);
    void delete(Long id);

    void update(Post post, User user);

    List<Post> getPostsByUserId(Long id);

    List<Post> filter(Optional<String> title, Optional<String> content, Optional<Integer> rating, Optional<String> createTime, Optional<String> updateTime, Optional<String> sort);

    List<Post> getAllSearch(Optional<String> all);

    List<Comment> getCommentsByPostId(Long id);

    void addOpinion(User user, Post post, Long id);

}
