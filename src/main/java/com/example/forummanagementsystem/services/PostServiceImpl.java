package com.example.forummanagementsystem.services;

import com.example.forummanagementsystem.exceptions.EntityDuplicateException;
import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.models.Comment;
import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository repository;

    @Autowired
    public PostServiceImpl(PostRepository repository) {
        this.repository = repository;
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

        if (duplicateExists) {
            throw new EntityDuplicateException("Post", "post", post.getTitle());
        }
        repository.update(post);
    }

    @Override
    public void update(Post post, User user) {
        repository.update(post);
    }

    @Override
    public List<Post> getPostsByUserId(Long id) {
        return repository.getPostsByUserId(id);
    }

    @Override
    public List<Post> filter(Optional<String> title, Optional<String> content, Optional<Integer> rating, Optional<String> sort) {
        //TODO authorization to be implemented
        return repository.filter(title, content, rating, sort);
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
        repository.delete(id);
    }
}

