package com.example.forummanagementsystem;

import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.User;

public class Helpers {

    public static Post createMockPost() {
        var mockPost = new Post();
        mockPost.setPostId(1L);
        mockPost.setTitle("Mock Title");
        mockPost.setContent("Mock content of the post №1");
        mockPost.setRating(5);
        return mockPost;
    }

public static User createMockUser() {
        var mockUser = new User();
        mockUser.setId(2L);
        mockUser.setUsername("mockUsername");
        mockUser.setFirstName("mockFirstName");
        mockUser.setLastName("mockLastName");
        mockUser.setEmail("mock@email.com");
        mockUser.setPassword("mockPassword");
        mockUser.setAdmin(false);
        return mockUser;
}

}
