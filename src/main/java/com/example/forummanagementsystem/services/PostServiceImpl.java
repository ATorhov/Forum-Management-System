package com.example.forummanagementsystem.services;

import com.example.forummanagementsystem.exceptions.BlockedUserException;
import com.example.forummanagementsystem.exceptions.EntityDuplicateException;
import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.models.Comment;
import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.repositories.CommentRepository;
import com.example.forummanagementsystem.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private static final String BLOCKED_USER_ERROR = "Blocked users are not allowed modifying posts, for more information please contact admin.";
    private final PostRepository repository;
    private final CommentRepository commentRepository;

    @Autowired
    public PostServiceImpl(PostRepository repository, CommentRepository commentRepository) {
        this.repository = repository;
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Post> getAll() {
        return repository.getAll();
    }

    @Override
    public Post getById(Long id) {
        return repository.getById(id);
    }

    @Override
    public Post getByTitle(String name) {
        return repository.getByTitle(name);
    }

    @Override
    public void create(Post post) {
        boolean duplicateExists = true;
        try {
            repository.getByTitle(post.getTitle());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
        if (post.getUser().isBlocked()){
            throw new BlockedUserException(BLOCKED_USER_ERROR);
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("Post", "title", post.getTitle());
        }

        repository.create(post);
    }

    @Override
    public void update(Post post) {
        boolean duplicateExists = true;
        try {
            Post existingPost = repository.getByTitle(post.getTitle());
            if (existingPost.getPostId() == post.getPostId()) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
        if (post.getUser().isBlocked()){
            throw new BlockedUserException(BLOCKED_USER_ERROR);
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("Post", "post", post.getTitle());
        }
        repository.update(post);
    }

    @Override
    public void update(Post post, User user) {
        if (post.getUser().isBlocked()){
            throw new BlockedUserException(BLOCKED_USER_ERROR);
        }
        repository.update(post);
    }

    @Override
    public List<Post> getPostsByUserId(Long id) {
        return repository.getPostsByUserId(id);
    }

    @Override
    public List<Post> filter(Optional<String> title,
                             Optional<String> content,
                             Optional<Integer> rating,
                             Optional<String> createTime,
                             Optional<String> updateTime,
                             Optional<String> sort) {

        LocalDateTime createDateTime = null;
        LocalDateTime updateDateTime = null;

        if (createTime.isPresent()) {
            createDateTime = LocalDateTime.parse(createTime.get());
        }

        if (updateTime.isPresent()) {
            updateDateTime = LocalDateTime.parse(updateTime.get());
        }

        return repository.filter(title, content, rating, Optional.ofNullable(createDateTime), Optional.ofNullable(updateDateTime), sort);
    }

    @Override
    public List<Post> getAllSearch(Optional<String> all) {
        return repository.search(all);
    }

    @Override
    public List<Comment> getCommentsByPostId(Long id) {
        return repository.getCommentsByPostId(id);
    }


    @Override
    public void delete(Long id) {
        if (getById(id).getUser().isBlocked()){
            throw new BlockedUserException(BLOCKED_USER_ERROR);
        }
        repository.delete(id);
    }
}