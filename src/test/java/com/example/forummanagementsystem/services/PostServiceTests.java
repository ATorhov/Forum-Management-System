package com.example.forummanagementsystem.services;

import com.example.forummanagementsystem.exceptions.EntityDuplicateException;
import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.repositories.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.forummanagementsystem.Helpers.createMockPost;

@ExtendWith(MockitoExtension.class)
public class PostServiceTests {

    @Mock
    PostRepository mockRepository;
    @InjectMocks
    PostServiceImpl mockService;

    @Test
    public void getById_Should_ReturnPost_When_MatchExists() {
        // Arrange
        Mockito.when(mockRepository.getById(2L))
                .thenReturn(new Post(2L, "Some Title", "Some body of the post"));

        // Act
        Post result = mockService.getById(2L);

        // Assert
        Assertions.assertEquals(2L, result.getPostId());
        Assertions.assertEquals("Some Title", result.getTitle());
        Assertions.assertEquals("Some body of the post", result.getContent());
    }

    @Test
    public void create_Should_Throw_When_PostWithSameTitleExists() {
        // Arrange
        var mockPost = createMockPost();

        Mockito.when(mockRepository.getByTitle(mockPost.getTitle()))
                .thenReturn(mockPost);
        // Act, Assert
        Assertions.assertThrows(EntityDuplicateException.class, () -> mockService.create(mockPost));
    }


}
