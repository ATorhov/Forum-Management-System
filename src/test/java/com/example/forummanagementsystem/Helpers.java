package com.example.forummanagementsystem;

import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.PostDto;
import com.example.forummanagementsystem.models.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

public class Helpers {

    public static Post createMockPost() {
        var mockPost = new Post();
        mockPost.setPostId(2L);
        mockPost.setTitle("Mock Title");
        mockPost.setContent("Mock content of the post №1");
        mockPost.setRating(5);
        mockPost.setCreateTime(LocalDateTime.now());
        mockPost.setUpdateTime(LocalDateTime.now());
        return mockPost;
    }

    public static Post createUniqueMockPost () {
        var mockPost = new Post();
        mockPost.setPostId(4L);
        mockPost.setTitle("Unique Mock Title");
        mockPost.setContent("Unique Mock content of the post №1");
        mockPost.setRating(6);
        mockPost.setCreateTime(LocalDateTime.now());
        mockPost.setUpdateTime(LocalDateTime.now());
        return mockPost;
    }

public static User createMockUser() {
        var mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("mockUsername");
        mockUser.setFirstName("mockFirstName");
        mockUser.setLastName("mockLastName");
        mockUser.setEmail("mock@email.com");
        mockUser.setPassword("mockPassword");
        mockUser.setAdmin(false);
        mockUser.setBlocked(false);
        mockUser.setRegisteredTime(LocalDateTime.now());
        return mockUser;
}

    public static PostDto createPostDto() {
        PostDto dto = new PostDto();
        dto.setTitle("DtoTitle");
        dto.setContent("DtoContent for Unittests");
        dto.setRating(5);
        dto.setCreateTime();
        dto.setUpdateTime(LocalDateTime.now());
        dto.setUser(createMockUser());
        return dto;
    }


    public static String toJson(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
