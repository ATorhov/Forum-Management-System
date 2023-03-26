package com.example.forummanagementsystem.controllers.rest;

import com.example.forummanagementsystem.exceptions.AuthorizationException;
import com.example.forummanagementsystem.exceptions.BlockedUserException;
import com.example.forummanagementsystem.exceptions.EntityDuplicateException;
import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.helpers.AuthenticationHelper;
import com.example.forummanagementsystem.models.Comment;
import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.PostDto;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.services.PostService;
import com.example.forummanagementsystem.services.mappers.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    private final PostMapper modelMapper;

    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public PostController(PostService postService, PostMapper modelMapper, AuthenticationHelper authenticationHelper) {
        this.postService = postService;
        this.modelMapper = modelMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<Post> getAll() {
        return postService.getAll();
    }

    @GetMapping("/search")
    public List<Post> getAllSearch(@RequestParam(required = false) Optional<String> search) {
        return postService.getAllSearch(search);
    }

    @GetMapping("/filter")
    public List<Post> filter(
            @RequestParam(required = false) Optional<String> title,
            @RequestParam(required = false) Optional<String> content,
            @RequestParam(required = false) Optional<Integer> rating,
            @RequestParam(required = false) Optional<String> createTime,
            @RequestParam(required = false) Optional<String> updateTime,
            @RequestParam(required = false) Optional<String> sort
    ) {
        try {
            return postService.filter(title, content, rating, createTime, updateTime, sort);
        } catch (UnsupportedOperationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @GetMapping("/user/{id}")
    public List<Post> getPostsByUserId(@PathVariable Long id) {
        try {
            return postService.getPostsByUserId(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Post getById(@PathVariable Long id) {
        try {
            return postService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{id}/comments")
    public List<Comment> getCommentsByPostId(@PathVariable Long id) {
        return postService.getCommentsByPostId(id);
    }

    @PostMapping
    public PostDto create(@RequestHeader HttpHeaders headers, @Valid @RequestBody PostDto postDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Post post = modelMapper.createDtoToObject(postDto, user);
            postService.create(post);
            return modelMapper.createObjectToDto(post);
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Post update(@RequestHeader HttpHeaders headers, @PathVariable Long id, @Valid @RequestBody PostDto postDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Post postToUpdate = postService.getById(id);
            authenticationHelper.checkPermissions(postToUpdate.getUser().getId(), user);

            // Only allow title and content to be updated by owners and admins
            postToUpdate.setTitle(postDto.getTitle());
            postToUpdate.setContent(postDto.getContent());
            if (user.isAdmin()) {
                postToUpdate.setRealRating();
            }

            postService.update(postToUpdate);
            return postToUpdate;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable Long id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            authenticationHelper.checkPermissions(postService.getById(id).getUser().getId(), user);
            postService.delete(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
