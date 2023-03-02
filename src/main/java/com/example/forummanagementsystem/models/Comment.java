package com.example.forummanagementsystem.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.lang.NonNull;

import javax.persistence.*;


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
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "post_id",referencedColumnName = "post_id", nullable = false)
    private Post post;

    public Comment() {
    }

    public Comment(Integer commentId, Post post, String content) {
        this.commentId = commentId;
        this.post =post;
        this.content = content;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Post getPostId() {
        return post;
    }

    public void setPostId(Post post) {
        this.post = post;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}