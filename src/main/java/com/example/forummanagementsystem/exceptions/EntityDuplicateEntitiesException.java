package com.example.forummanagementsystem.exceptions;

public class EntityDuplicateEntitiesException extends RuntimeException {
    public EntityDuplicateEntitiesException(int commentId, int postId) {
        super(String.format("comment %s of post %s  already exists.", commentId, postId));
    }
}
