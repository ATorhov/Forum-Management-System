package com.example.forummanagementsystem.models.dtos;

import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.User;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class CommentDto {

    @NotNull(message = "Content cannot be empty")
    @Size(min = 2, max = 2000, message = "Comment content should be between 2 and 2000 symbols inclusively")
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
