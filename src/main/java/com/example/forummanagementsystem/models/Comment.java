package com.example.forummanagementsystem.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;


@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Integer commentId;


    @NotNull(message = "Content cannot be empty")
    @Column(name = "content")
    @Size(min = 2, max = 2000, message = "Comment content should be between 2 and 2000 symbols inclusively")
    private String content;

    @NonNull
    @ManyToOne
    @JsonIgnore
    private Post post;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    public Comment() {
    }

    public Comment(Integer commentId, Post post, String content) {
        this.commentId = commentId;
        this.post = post;
        this.content = content;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    @NonNull
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @NonNull
    public User getUser() {
        return user;
    }

    public void setUser(@NonNull User user) {
        this.user = user;
    }

    @NonNull
    public Post getPost() {
        return post;
    }

    public void setPost(@NonNull Post post) {
        this.post = post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return commentId == comment.commentId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId);
    }
}