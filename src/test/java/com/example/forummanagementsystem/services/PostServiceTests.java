package com.example.forummanagementsystem.services;

import com.example.forummanagementsystem.exceptions.BlockedUserException;
import com.example.forummanagementsystem.exceptions.EntityDuplicateException;
import com.example.forummanagementsystem.models.Comment;
import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.PostFilterOptions;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.repositories.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.forummanagementsystem.Helpers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        when(mockRepository.getAll()).thenReturn(null);
        //Act
        mockService.getAll();
        //Assert
        verify(mockRepository, times(1)).getAll();
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
        assertEquals(post, result);
    }

    @Test
    public void create_Should_Throw_When_PostWithSameTitleExists() {
        // Arrange
        var mockPost = createMockPost();
        var mockUser = createMockUser();
        mockPost.setUser(mockUser);

        when(mockRepository.getByTitle(mockPost.getTitle())).thenReturn(mockPost);
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

    @Test
    public void testGet() {
        // Given
        PostFilterOptions filterOptions = new PostFilterOptions();
        List<Post> posts = new ArrayList<>(Arrays.asList(new Post(), new Post()));
        when(mockRepository.get(filterOptions)).thenReturn(posts);

        // When
        List<Post> result = mockService.get(filterOptions);

        // Then
        assertEquals(2, result.size());
        verify(mockRepository, times(1)).get(filterOptions);
    }

    @Test
    public void testGetById() {
        // Given
        Post post = new Post();
        when(mockRepository.getById(1L)).thenReturn(post);

        // When
        Post result = mockService.getById(1L);

        // Then
        assertEquals(post, result);
        verify(mockRepository, times(1)).getById(1L);
    }

    @Test
    public void testGetByTitle() {
        // Given
        Post post = new Post();
        when(mockRepository.getByTitle("title")).thenReturn(post);

        // When
        Post result = mockService.getByTitle("title");

        // Then
        assertEquals(post, result);
        verify(mockRepository, times(1)).getByTitle("title");
    }

    @Test
    public void testGetPostsByUserId() {
        // Given
        User user = new User();
        List<Post> posts = new ArrayList<>(Arrays.asList(new Post(), new Post()));
        when(mockRepository.getPostsByUserId(1L)).thenReturn(posts);

        // When
        List<Post> result = mockService.getPostsByUserId(1L);

        // Then
        assertEquals(2, result.size());
        verify(mockRepository, times(1)).getPostsByUserId(1L);
    }


    @Test
    public void testFilter() {
        // Given
        List<Post> posts = new ArrayList<>(Arrays.asList(new Post(), new Post()));
        when(mockRepository.filter(Optional.of("title"),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty())).thenReturn(posts);

        // When
        List<Post> result = mockService.filter(Optional.of("title"),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty());

        // Then
        assertEquals(2, result.size());
        verify(mockRepository, times(1)).filter(Optional.of("title"),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty());
    }

    @Test
    public void update_post_successful() {
        Post post = createMockPost();
        User user = createMockUser();
        post.setUser(user);

        Mockito.when(mockRepository.getByTitle("Mock Title")).thenReturn(post);
        mockService.update(post);
        Mockito.verify(mockRepository).update(post);
    }

    @Test
    public void update_post_blocked_user_exception() {
        Post post = createMockPost();
        User user = createMockUser();
        post.setUser(user);
        user.setBlocked(true);

        Mockito.when(mockRepository.getByTitle("Mock Title")).thenReturn(post);

        assertThrows(BlockedUserException.class,
                () -> mockService.update(post));
    }


    @Test
    public void delete_post_successful() {
        Post post = createMockPost();
        User user = createMockUser();
        post.setUser(user);

        Mockito.when(mockService.getById(post.getPostId())).thenReturn(post);

        mockService.delete(post.getPostId());

        Mockito.verify(mockRepository).delete(post.getPostId());
    }

    @Test
    public void getPostsCount_returnsCorrectCount() {
        Mockito.when(mockRepository.getPostsCount()).thenReturn(10);

        int count = mockService.getPostsCount();

        assertEquals(10, count);
    }

    @Test
    public void findTenMostRecentCreatedPosts_returnsCorrectPosts() {
        List<Post> mockPosts = createMockPosts(20);
        Mockito.when(mockRepository.getAll()).thenReturn(mockPosts);

        List<Post> recentPosts = mockService.findTenMostRecentCreatedPosts();

        assertEquals(10, recentPosts.size());
    }

    @Test
    public void findTenMostRatedPosts_returnsCorrectPosts() {
        List<Post> mockPosts = createMockPosts(15);
        mockPosts.get(0).setRealRating();
        mockPosts.get(1).setRealRating();
        mockPosts.get(2).setRealRating();

        Mockito.when(mockRepository.getAll()).thenReturn(mockPosts);

        List<Post> topRatedPosts = mockService.findTenMostRatedPosts();

        assertEquals(10, topRatedPosts.size());
    }
}