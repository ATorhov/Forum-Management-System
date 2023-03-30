package com.example.forummanagementsystem.repositories;

import com.example.forummanagementsystem.models.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PostRepository {


    List<Post> getAll();

    List<Post> get(PostFilterOptions filterOptions);
    Post getById(Long id);

    Post getByTitle(String name);

    void create(Post post);
    void update(Post post);
    void delete(Long id);

    List<Post> getPostsByUserId(Long userId);

    List<Post> filter(Optional<String> title, Optional<String> content, Optional<Integer> rating, Optional<LocalDateTime> createTime, Optional<LocalDateTime> updateTime, Optional<String> sort);

    void update(Post post, User user);

    List<Post> search(Optional<String> all);

    List<Comment> getCommentsByPostId(Long id);

    int getPostsCount();

    Map<User, Opinion> getOpinionsByPostId(Long id);
}
