package com.example.forummanagementsystem.models;

import javax.validation.constraints.NotNull;


public class CommentDto {

    private int post_id;

    @NotNull(message = "Content cannot be empty")
    private String content;


    public CommentDto() {
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
