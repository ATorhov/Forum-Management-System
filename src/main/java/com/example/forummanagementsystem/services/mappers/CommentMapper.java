package com.example.forummanagementsystem.services.mappers;

import com.example.forummanagementsystem.models.Comment;
import com.example.forummanagementsystem.models.CommentDto;
import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.repositories.CommentRepository;
import com.example.forummanagementsystem.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class CommentMapper {


    private final CommentRepository commentRepository;
    private final PostRepository postRepository;


    @Autowired
    public CommentMapper(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    public Comment fromDtoComment(CommentDto commentDto) {
        Comment comment = new Comment();
        dtoToObjectComment(commentDto, comment);
        return comment;
    }

    public Comment fromDtoComment(CommentDto commentDto, Integer id) {
        Comment comment = commentRepository.getById(id);
        dtoToObjectComment(commentDto, comment);
        return comment;
    }

    private void dtoToObjectComment(CommentDto commentDto, Comment comment) {
        Post post = postRepository.getById((long) commentDto.getPost_id());

        // comment.setPostId(commentDto.setPost_id(new Post()));
        comment.setContent(commentDto.getContent());
        post.setPostId(post.getPostId());

    }
}
