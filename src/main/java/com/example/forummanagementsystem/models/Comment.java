package com.example.forummanagementsystem.models;

public class Comment {
    private Integer commentId;
    private int postId;
    private String content;


    public Comment(Integer commentId, int postId, String content) {
        this.commentId = commentId;
        this.postId = postId;
        this.content = content;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}