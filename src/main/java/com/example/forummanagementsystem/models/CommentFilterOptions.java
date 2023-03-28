package com.example.forummanagementsystem.models;

import java.util.Optional;

public class CommentFilterOptions {

    private final Optional<String> content;
    private final Optional<Integer> commentId;
    private final Optional<String> sortBy;
    private final Optional<String> sortOrder;

    private final Optional<Post> post;
    private final Optional<User> user;
    private final Optional<Long> postId;
    private final Optional<Long> userId;


    public CommentFilterOptions() {
        this(null, null, null, null, null, null, null, null);
    }


    public CommentFilterOptions(
            String content,
            Integer commentId,
            String sortBy,
            String sortOrder,
            Post post,
            User user,
            Long postId,
            Long userId) {
        this.content = Optional.ofNullable(content);
        this.commentId = Optional.ofNullable(commentId);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
        this.post = Optional.ofNullable(post);
        this.user = Optional.ofNullable(user);
        this.postId = Optional.ofNullable(postId);
        this.userId = Optional.ofNullable(userId);
    }


    public Optional<String> getContent() {
        return content;
    }

    public Optional<Integer> getCommentId() {
        return commentId;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }

    public Optional<Post> getPost() {
        return post;
    }

    public Optional<User> getUser() {
        return user;
    }

    public Optional<Long> getPostId() {
        return postId;
    }

    public Optional<Long> getUserId() {
        return userId;
    }
}