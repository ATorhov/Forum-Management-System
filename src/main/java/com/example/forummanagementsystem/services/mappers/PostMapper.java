package com.example.forummanagementsystem.services.mappers;

import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.PostDto;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.repositories.PostRepository;
import com.example.forummanagementsystem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PostMapper {

    private final PostRepository postRepository;

    @Autowired
    public PostMapper(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
    }

    public Post fromDto(PostDto postDto) {
        Post post = new Post();
        dtoToObject(postDto, post);
        return post;
    }

    public Post fromDto(PostDto postDto, long id) {
        Post post = postRepository.getById(id);
        dtoToObject(postDto, post);
        return post;
    }

    private void dtoToObject(PostDto postDto, Post post) {
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setRating(postDto.getRating());
        post.setUpdateTime(LocalDateTime.now());
    }

    public Post createDtoToObject(PostDto postDto, User user) {
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setCreateTime(LocalDateTime.now());
        post.setUpdateTime(post.getCreateTime());
        post.setUser(user);
        post.setRating(postDto.getRating());
        return post;
    }

    public Post createDtoToObject(PostDto postDto, User user, Long id) {
        Post post = createDtoToObject(postDto, user);
        post.setPostId(id);
        return post;
    }


    public PostDto createObjectToDto(Post post){
        PostDto postDto = new PostDto();
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        postDto.setCreateTime();
        postDto.setRating(post.getRating());
        return postDto;
    }

}
