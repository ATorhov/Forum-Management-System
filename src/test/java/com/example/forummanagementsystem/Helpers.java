package com.example.forummanagementsystem;

import com.example.forummanagementsystem.models.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Helpers {

    public static Post createMockPost() {
        var mockPost = new Post();
        mockPost.setPostId(2L);
        mockPost.setTitle("Mock Title");
        mockPost.setContent("Mock content of the post №1");
        mockPost.setRealRating();
        mockPost.setCreateTime(LocalDateTime.now());
        mockPost.setUpdateTime(LocalDateTime.now());
        return mockPost;
    }

    public static Post createUniqueMockPost() {
        var mockPost = new Post();
        mockPost.setPostId(4L);
        mockPost.setTitle("Unique Mock Title");
        mockPost.setContent("Unique Mock content of the post №1");
        mockPost.setRealRating();
        mockPost.setCreateTime(LocalDateTime.now());
        mockPost.setUpdateTime(LocalDateTime.now());
        return mockPost;
    }

    public static List<Post> createMockPosts(int count) {
        List<Post> posts = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            var post = new Post();
            post.setPostId((long) i + 1);
            post.setTitle("Mock Title " + (i + 1));
            post.setContent("Mock content of the post №" + (i + 1));
            post.setRealRating();
            post.setCreateTime(LocalDateTime.now());
            post.setUpdateTime(LocalDateTime.now());
            posts.add(post);
        }
        return posts;
    }

    public static List<Comment> createMockComments(int count) {
        List<Comment> comments = new ArrayList<>();
        Post post = createMockPost();
        for (int i = 0; i < count; i++) {
            var comment = createMockComment();
            comment.setCommentId(i + 1);
            comment.setContent("mock "+i+1);
        }
        return comments;
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

    public static User createMockAdmin() {
        User mockUser = createMockUser();
        mockUser.setAdmin(true);
        return mockUser;
    }

    public static Comment createMockComment() {
        var mockComment = new Comment();
        mockComment.setCommentId(2);
        mockComment.setContent("content");
        Post mockPost = createMockPost();
        User mockUser = createMockUser();
        mockComment.setPost(mockPost);
        mockComment.setUser(mockUser);
        return mockComment;
    }
}
