package com.example.forummanagementsystem.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.lang.NonNull;

import javax.persistence.*;

import java.util.Objects;


@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Integer commentId;


    @Column(name = "content")
    private String content;

    @NonNull
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Post post;

    @ManyToOne(cascade = CascadeType.REMOVE)
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