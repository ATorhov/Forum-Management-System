package com.example.forummanagementsystem.controllers;

import com.example.forummanagementsystem.services.CommentService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.example.forummanagementsystem.Helpers.createMockComment;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTests {

    @MockBean
    CommentService mockService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void getById_should_Return_StatusOk_When_CommentExists() throws Exception {

        //Arrange
        var mockComment = createMockComment();
        Mockito.when(mockService.getById(mockComment.getCommentId()))
                .thenReturn(mockComment);

        //Act, Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/comments/{id}", mockComment.getCommentId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(mockComment.getContent()));
    }
}