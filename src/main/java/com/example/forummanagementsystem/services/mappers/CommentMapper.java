package com.example.forummanagementsystem.services.mappers;

import com.example.forummanagementsystem.models.Comment;
import com.example.forummanagementsystem.models.CommentDto;
import com.example.forummanagementsystem.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class CommentMapper {


        private final CommentRepository commentRepository;


        @Autowired
        public CommentMapper(CommentRepository commentRepository) {
            this.commentRepository = commentRepository;
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
            comment.setPostId(commentDto.getPost_id());
            comment.setContent(commentDto.getContent());
        }
}
