package com.example.forummanagementsystem.services;

import com.example.forummanagementsystem.models.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PostService {

    List<Post> getAll();
    List<Post> getAll(User user);

    List<Post> get(PostFilterOptions postFilterOptions);
    Post getById(Long id);

    Post getByTitle(String name);

    void create(Post post);
    void update(Post post);
    void delete(Long id);

    void update(Post post, User user);

    List<Post> getPostsByUserId(Long id);

    List<Post> filter(Optional<String> title, Optional<String> content, Optional<Long> rating, Optional<String> createTime, Optional<String> updateTime, Optional<String> sort);

    List<Post> getAllSearch(Optional<String> all);

    List<Comment> getCommentsByPostId(Long id);

    void addOpinion(User user, Post post, Long id);

    int getPostsCount();

    List<Post> findTenMostCommentedPosts();

    public List<Post> findTenMostRecentCreatedPosts();

    List<Post> findTenMostRatedPosts();

    long getLikes(Post post);
    long getDislikes(Post post);

    Map<User, Opinion> getOpinionsByPostId(Long id);
}
