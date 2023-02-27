package com.example.forummanagementsystem.services;

import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.PostDto;
import com.example.forummanagementsystem.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ModelMapper {

    private final PostRepository postRepository;
    //private final UserRepository userRepository;

    @Autowired
    public ModelMapper(PostRepository postRepository) {
        this.postRepository = postRepository;

    }

    public Post fromDto(PostDto postDto){
        Post post = new Post();
        dtoToObject(postDto, post);
        return post;
    }

    public Post fromDto(PostDto postDto, int id){
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


//    public User fromDto(UserDto userDto){
//        User user = new User();
//        dtoToObject(userDto, user);
//        return user;
//    }
//
//    public User fromDto(UserDto userDto, int id){
//        User user = userRepository.getById(id);
//        dtoToObject(userDto, user);
//        return user;
//    }
//
//    private void dtoToObject(UserDto userDto, User user){
//        user.setFirstName(userDto.getFirstName());
//        user.setLastName(userDto.getLastName());
//        user.setPassword(user.getPassword());
//        user.setEmail(userDto.getEmail());
//        user.setUsername(user.getUsername());
//    }
}
