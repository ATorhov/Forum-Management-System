package com.example.forummanagementsystem.services;

import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.PostDto;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.models.UserDto;
import com.example.forummanagementsystem.repositories.PostRepository;
import com.example.forummanagementsystem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ModelMapper {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public ModelMapper(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Post fromDto(PostDto postDto){
        Post post = new Post();
        dtoToObject(postDto, post);
        return post;
    }

    public Post fromDto(PostDto postDto, long id){
        Post post = postRepository.getById(id);
        dtoToObject(postDto, post);
        return post;
    }

    private void dtoToObject(PostDto postDto, Post post){
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setRating(postDto.getRating());
        post.setUpdateTime(LocalDateTime.now());
    }


    public User fromDto(UserDto userDto){
        User user = new User();
        dtoToObject(userDto, user);
        return user;
    }

    public User fromDto(UserDto userDto, Long id){
        User user = userRepository.get(id);
        dtoToObject(userDto, user);
        return user;
    }

    private void dtoToObject(UserDto userDto, User user){
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        user.setPhoneNumber(userDto.getPhoneNumber());
    }
}
