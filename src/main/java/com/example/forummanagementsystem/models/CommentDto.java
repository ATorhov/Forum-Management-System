package com.example.forummanagementsystem.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;


public class CommentDto {

   // @Positive(message = "post_id should be positive")
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
