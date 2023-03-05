package com.example.forummanagementsystem.models;

import org.springframework.lang.NonNull;

import javax.validation.constraints.NotNull;


public class CommentDto {


    private int post_id;

    @NotNull(message = "Content cannot be empty")
    private String content;


    private User user;

    private Post post;


    public CommentDto() {
    }


    @NonNull
    public User getUser() {
        return user;
    }

    public void setUser(@NonNull User user) {
        this.user = user;
    }


    public Post getPost() {
        return post;
    }

    public void setPost(@NonNull Post post) {
        this.post = post;
    }

//    public int getPost_id() {
//        return post_id;
//    }
//
//    public void setPost_id(int post_id) {
//        this.post_id = post_id;
//    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
