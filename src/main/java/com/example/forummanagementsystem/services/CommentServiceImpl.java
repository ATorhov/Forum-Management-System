package com.example.forummanagementsystem.services;

import com.example.forummanagementsystem.models.Comment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.forummanagementsystem.repositories.CommentRepository;



@Service
public class CommentServiceImpl implements CommentService {


    private final CommentRepository repository;

    @Autowired
    public CommentServiceImpl(CommentRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Comment> getAll() {
        return repository.getAll();
    }

    @Override
    public Comment getById(int id) {
        return repository.getById(id);
    }


    @Override
    public void create(Comment comment) {

        repository.create(comment);
    }

    @Override
    public void update(Comment comment) {


        repository.update(comment);
    }

    @Override
    public void delete(int id) {
        repository.delete(id);
    }
}


