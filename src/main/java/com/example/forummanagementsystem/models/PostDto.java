package com.example.forummanagementsystem.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.lang.NonNull;

import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

public class PostDto {

    private String title;

    private int rating;

    private String content;

    @JsonIgnore
    @CreationTimestamp
    private LocalDateTime createTime;

    @JsonIgnore
    @UpdateTimestamp
    private LocalDateTime updateTime;

//    @NonNull
//    @ManyToOne
//    private User user;

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

    private void setCreateTime(LocalDateTime createTime) {
        this.createTime = LocalDateTime.now();
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

//    @NonNull
//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(@NonNull User user) {
//        this.user = user;
//    }

}
