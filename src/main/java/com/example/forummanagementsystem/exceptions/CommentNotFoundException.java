package com.example.forummanagementsystem.exceptions;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(int commentId, int postId) {
        super(String.format("Comment %s of post %s not found", commentId, postId));
    }
}