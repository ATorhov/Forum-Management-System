package com.example.forummanagementsystem.services.mappers;

import com.example.forummanagementsystem.models.*;
import com.example.forummanagementsystem.models.dtos.CommentDto;
import com.example.forummanagementsystem.repositories.UserRepository;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.repositories.CommentRepository;
import com.example.forummanagementsystem.repositories.PostRepository;
import com.example.forummanagementsystem.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
public class CommentMapper {


    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentService commentService;


    @Autowired
    public CommentMapper(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository, CommentService commentService) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.commentService = commentService;
    }


    public Comment fromDto(int id, CommentDto commentDto) {
        Comment comment = fromDto(commentDto);
        comment.setCommentId(id);
        Comment repositoryComment = commentService.getById(id);
        comment.setUser(repositoryComment.getUser());

        comment.setPost(repositoryComment.getPost());
        return comment;
    }

    public Comment fromDto(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());

        return comment;
    }


    public Comment dtoToObjectComment(CommentDto commentDto, User user, Post post) {
        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        comment.setUser(user);
        comment.setPost(post);
        return comment;
    }

    public Comment dtoToObjectCommentForCreate(CommentDto commentDto, User user, Post post) {
        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        comment.setUser(user);
        comment.setPost(post);

        return comment;
    }

    public Comment createDtoToObject(CommentDto commentDto, User user, int id) {

        Comment comment = commentRepository.getById(id);
        comment.setContent(commentDto.getContent());
        return comment;
    }

    public Comment createDtoToObject(CommentDto commentDto, User user) {
        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        comment.setUser(user);
        return comment;
    }

    public CommentDto createObjectToDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setContent(comment.getContent());
        return commentDto;
    }
}
