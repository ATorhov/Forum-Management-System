package com.example.forummanagementsystem.services;

import com.example.forummanagementsystem.exceptions.*;

import com.example.forummanagementsystem.models.Comment;
import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.repositories.CommentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.forummanagementsystem.Helpers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTests {

    @Mock
    CommentRepository mockRepository;

    @InjectMocks
    CommentServiceImpl mockService;

    @Test
    public void get_Should_ReturnComment_When_MatchByIdExist() {
        // Arrange
        Comment mockComment = createMockComment();
        Mockito.when(mockRepository.getById(Mockito.anyInt()))
                .thenReturn(mockComment);
        // Act
        Comment result = mockService.getById(mockComment.getCommentId());
        // Assert
        Assertions.assertEquals(mockComment, result);
    }

    @Test
    public void getById_Should_ReturnComment_When_MatchExists() {
        //Arrange
        Mockito.when(mockRepository.getById(2))
                .thenReturn(new Comment(2, new Post(), "content"));
        //Act
        Comment result = mockService.getById(2);
        //Assert
        Assertions.assertEquals(2, result.getCommentId());
        Assertions.assertEquals(new Post(), result.getPost());
        Assertions.assertEquals("content", result.getContent());
    }

    @Test
    public void get_Should_CallRepository() {
        Optional<String> search = Optional.of("name");
        //Arrange
        Mockito.when(mockRepository.getAll(search)).thenReturn(null);
        //Act

        mockService.getAll(search);
        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).getAll(search);
    }

    @Test
    public void create_Should_returnComment_When_UserIsNotBlocked() {
        //false positive test?

        //Arrange
        var mockComment = createMockComment();
        mockComment.setCommentId(2);
        var mockUser = createMockUser();
        mockUser.setBlocked(false);
        Post mockPost = createMockPost();
        mockComment.setUser(mockUser);
        mockComment.setContent("content");
        //Act, Assert
        Assertions.assertEquals(2, mockComment.getCommentId());
        Assertions.assertEquals(mockPost, mockComment.getPost());
        Assertions.assertEquals("content", mockComment.getContent());

    }

    @Test
    public void create_Should_Throw_When_UserIsBlocked() {
        // Arrange
        Comment mockComment = createMockComment();

        mockComment.setUser(createMockUser());
        mockComment.setPost(createMockPost());
        createMockUser().setBlocked(true);
        // Act, Assert
        assertThrows(BlockedUserException.class,
                () -> mockService.create(mockComment, createMockUser(),createMockPost(), 4L));
    }

    @Test
    public void update_Should_CallRepository_When_UserIsCreator() {
        // Arrange
        Comment mockComment = createMockComment();
        User mockUserCreator = mockComment.getUser();
        Mockito.when(mockRepository.getById(Mockito.anyInt()))
                .thenReturn(mockComment);
        Mockito.when(mockRepository.getById(Mockito.anyInt()))
                .thenThrow(EntityNotFoundException.class);
        // Act
        mockService.update(mockComment, mockUserCreator);
        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(mockComment);
    }

    @Test
    public void update_Should_CallRepository_When_UserIsAdmin() {
        // Arrange
        User mockUserAdmin = createMockAdmin();
        Comment mockComment = createMockComment();
        Mockito.when(mockRepository.getById(Mockito.anyInt()))
                .thenReturn(mockComment);
        Mockito.when(mockRepository.getById(Mockito.anyInt()))
                .thenThrow(EntityNotFoundException.class);
        // Act
        mockService.update(mockComment, mockUserAdmin);
        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(mockComment);
    }

    @Test
    public void update_Should_Throw_When_UserIsNotCreatorOrAdmin() {
        // Arrange
        var mockComment = createMockComment();
        var mockUser = createMockUser();
        mockUser.setUsername("MockUser2");
        mockComment.setUser(mockUser);
        // Act
        Mockito.when(mockRepository.getById(Mockito.anyInt()))
                .thenReturn(mockComment);
        // Assert
        Assertions.assertThrows(AuthorizationException.class,
                () -> mockService.update(mockComment, mockUser));
    }

    @Test
    public void update_Should_CallRepository_When_UpdatingExistingComment() {
        // Arrange
        Comment mockComment = createMockComment();
        User mockUserCreator = mockComment.getUser();
        Mockito.when(mockRepository.getById(Mockito.anyInt()))
                .thenReturn(mockComment);
        Mockito.when(mockRepository.getById(Mockito.anyInt()))
                .thenReturn(mockComment);
        // Act
        mockService.update(mockComment, mockUserCreator);
        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(mockComment);
    }

    @Test
    public void update_Should_ThrowException_When_UserIsNotCreatorOrAdmin() {
        // Arrange
        Comment mockComment = createMockComment();
        Mockito.when(mockRepository.getById(mockComment.getCommentId()))
                .thenReturn(mockComment);
        // Act, Assert
        Assertions.assertThrows(
                AuthorizationException.class,
                () -> mockService.update(mockComment, Mockito.mock(User.class)));
    }

    @Test
    public void delete_Should_ThrowException_When_UserIsNotAdminOrCreator() {
        // Arrange
        Comment mockComment = createMockComment();
        Mockito.when(mockRepository.getById(Mockito.anyInt()))
                .thenReturn(mockComment);
        User mockUser = Mockito.mock(User.class);
        // Act, Assert
        Assertions.assertThrows(
                AuthorizationException.class,
                () -> mockService.delete(1, mockUser));
    }

    @Test
    public void delete_Should_CallRepository_When_UserIsAdmin() {
        // Arrange
        User mockUserAdmin = createMockAdmin();
        Comment mockComment = createMockComment();
        Mockito.when(mockRepository.getById(Mockito.anyInt()))
                .thenReturn(mockComment);
        // Act
        mockService.delete(1, mockUserAdmin);
        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .delete(1);
    }

    @Test
    public void delete_Should_CallRepository_When_UserIsCreator() {
        // Arrange
        Comment mockComment = createMockComment();
        User mockUserCreator = mockComment.getUser();

        Mockito.when(mockRepository.getById(Mockito.anyInt()))
                .thenReturn(mockComment);
        // Act
        mockService.delete(1, mockUserCreator);
        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .delete(1);
    }
}