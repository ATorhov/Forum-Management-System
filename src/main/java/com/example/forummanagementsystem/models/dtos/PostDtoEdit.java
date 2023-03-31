package com.example.forummanagementsystem.models.dtos;

import com.example.forummanagementsystem.models.Opinion;
import com.example.forummanagementsystem.models.User;

import javax.validation.constraints.Size;
import java.util.Map;

public class PostDtoEdit {

    private Long id;

    @Size(min = 16, max = 64, message = "Title must be between 16 and 64 symbols")
    private String title;

    @Size(min = 32, max = 8192, message = "Content must be between 32 and 8192 symbols")
    private String content;

    private Map<User, Opinion> opinions;

    public Map<User, Opinion> getOpinions() {
        return opinions;
    }

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setOpinions(Map<User, Opinion> opinions) {
        this.opinions = opinions;
    }

    public PostDtoEdit() {
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId (Long id) {
        this.id = id;
    }
}

