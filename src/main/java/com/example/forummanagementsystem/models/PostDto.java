package com.example.forummanagementsystem.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class PostDto {

    @NotNull(message = "Post title can't be empty")
    @Size(min = 16, max = 32, message = "Post title should be between 16 and 64 symbols")
    private String title;

    private int rating;

    @NotNull(message = "Post content can't be empty")
    @Size(min = 32, max = 8192, message = "Post content should be between 16 and 8192 symbols")
    private String content;

    private LocalDateTime createTime;

    private User user;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime() {
        this.createTime = LocalDateTime.now();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
