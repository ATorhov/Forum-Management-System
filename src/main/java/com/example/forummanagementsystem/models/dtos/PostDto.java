package com.example.forummanagementsystem.models.dtos;

import com.example.forummanagementsystem.models.Opinion;
import com.example.forummanagementsystem.models.User;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Map;

public class PostDto {

    @NotNull(message = "Post title can't be empty")
    @Size(min = 16, max = 32, message = "Post title should be between 16 and 64 symbols")
    private String title;

    private long rating;

    @NotNull(message = "Post content can't be empty")
    @Size(min = 32, max = 8192, message = "Post content should be between 16 and 8192 symbols")
    private String content;

    private LocalDateTime createTime;
    private User user;

    private Map<User, Opinion> opinions;

    public PostDto(){

    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }


    public Map<User, Opinion> getOpinions() {
        return opinions;
    }

    public void setOpinions(Map<User, Opinion> opinions) {
        this.opinions = opinions;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
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
