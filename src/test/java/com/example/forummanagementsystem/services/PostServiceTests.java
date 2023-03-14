package com.example.forummanagementsystem.services;

import com.example.forummanagementsystem.exceptions.BlockedUserException;
import com.example.forummanagementsystem.exceptions.EntityDuplicateException;
import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.repositories.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.forummanagementsystem.Helpers.createMockPost;
import static com.example.forummanagementsystem.Helpers.createMockUser;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class PostServiceTests {

    @Mock
    PostRepository mockRepository;
    @InjectMocks
    PostServiceImpl mockService;

    @BeforeEach
    public void setUp() {
        Mockito.reset(mockRepository);
    }

    @Test
    public void get_Should_CallRepository() {
        //Arrange
        Mockito.when(mockRepository.getAll()).thenReturn(null);
        //Act
        mockService.getAll();
        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).getAll();
    }

    @Test
    public void getById_Should_ReturnPost_When_MatchExists() {
        // Arrange
        Post post = createMockPost();
        Mockito.when(mockRepository.getById(Mockito.anyLong()))
                .thenReturn(post);

        // Act
        Post result = mockService.getById(post.getPostId());

        // Assert
        Assertions.assertEquals(post, result);
    }

    @Test
    public void create_Should_Throw_When_PostWithSameTitleExists() {
        // Arrange
        var mockPost = createMockPost();
        var mockUser = createMockUser();
        mockPost.setUser(mockUser);

        Mockito.when(mockRepository.getByTitle(mockPost.getTitle())).thenReturn(mockPost);
        // Act, Assert
        assertThrows(EntityDuplicateException.class, () -> mockService.create(mockPost));
    }

    @Test
    public void create_Should_returnPost_When_UserIsNotBlocked() {
        //Arrange
        var mockUser = createMockUser();
        mockUser.setBlocked(false);
        var mockPost = createMockPost();
        mockPost.setUser(mockUser);

        //Act, Assert
        assertThrows(EntityDuplicateException.class,
                () -> mockService.create(mockPost));
    }

    @Test
    public void create_Should_Throw_When_UserIsBlocked() {
// Arrange
        User mockUser = createMockUser();
        mockUser.setBlocked(true);
        Post mockPost = createMockPost();
        mockPost.setUser(mockUser);

        // Act, Assert
        assertThrows(BlockedUserException.class,
                () -> mockService.create(mockPost));

    }
}
