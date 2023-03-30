package com.example.forummanagementsystem.services;

import com.example.forummanagementsystem.exceptions.AuthorizationException;
import com.example.forummanagementsystem.exceptions.BlockedUserException;
import com.example.forummanagementsystem.exceptions.EntityDuplicateException;
import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.models.*;
import com.example.forummanagementsystem.repositories.CommentRepository;
import com.example.forummanagementsystem.repositories.OpinionRepository;
import com.example.forummanagementsystem.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private static final String BLOCKED_USER_ERROR = "Blocked users are not allowed modifying posts, for more information please contact admin.";
    private final PostRepository repository;
    private final CommentRepository commentRepository;

    private final OpinionRepository opinionRepository;

    @Autowired
    public PostServiceImpl(PostRepository repository, CommentRepository commentRepository, OpinionRepository opinionRepository) {
        this.repository = repository;
        this.commentRepository = commentRepository;
        this.opinionRepository = opinionRepository;
    }

    @Override
    public List<Post> getAll() {
        return repository.getAll();
    }

    @Override
    public List<Post> getAll(User user) {
        if (!user.isAdmin()) {
            throw new AuthorizationException("Only admin can see all the users!");
        }
        return repository.getAll();
    }

    @Override
    public List<Post> get(PostFilterOptions postFilterOptions) {
        return repository.get(postFilterOptions);
    }

    @Override
    public Post getById(Long id) {
        return repository.getById(id);
    }

    @Override
    public Post getByTitle(String name) {
        return repository.getByTitle(name);
    }

    @Override
    public void create(Post post) {
        boolean duplicateExists = true;
        try {
            repository.getByTitle(post.getTitle());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
        if (post.getUser().isBlocked()){
            throw new BlockedUserException(BLOCKED_USER_ERROR);
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("Post", "title", post.getTitle());
        }

        repository.create(post);
    }

    @Override
    public void update(Post post) {
        boolean duplicateExists = true;
        try {
            Post existingPost = repository.getByTitle(post.getTitle());
            if (existingPost.getPostId() == post.getPostId()) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
        if (post.getUser().isBlocked()){
            throw new BlockedUserException(BLOCKED_USER_ERROR);
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("Post", "post", post.getTitle());
        }
        repository.update(post);
    }

    @Override
    public void update(Post post, User user) {
        if (post.getUser().isBlocked()){
            throw new BlockedUserException(BLOCKED_USER_ERROR);
        }
        repository.update(post);
    }

    @Override
    public void delete(Long id) {
        if (getById(id).getUser().isBlocked()){
            throw new BlockedUserException(BLOCKED_USER_ERROR);
        }
        repository.delete(id);
    }

    @Override
    public List<Post> getPostsByUserId(Long id) {
        return repository.getPostsByUserId(id);
    }

    @Override
    public List<Post> filter(Optional<String> title,
                             Optional<String> content,
                             Optional<Integer> rating,
                             Optional<String> createTime,
                             Optional<String> updateTime,
                             Optional<String> sort) {

        LocalDateTime createDateTime = null;
        LocalDateTime updateDateTime = null;

        if (createTime.isPresent()) {
            createDateTime = LocalDateTime.parse(createTime.get());
        }

        if (updateTime.isPresent()) {
            updateDateTime = LocalDateTime.parse(updateTime.get());
        }

        return repository.filter(title, content, rating, Optional.ofNullable(createDateTime), Optional.ofNullable(updateDateTime), sort);
    }

    @Override
    public List<Post> getAllSearch(Optional<String> all) {
        return repository.search(all);
    }

    @Override
    public List<Comment> getCommentsByPostId(Long id) {
        return repository.getCommentsByPostId(id);
    }

    @Override
    public Map<User, Opinion> getOpinionsByPostId(Long id) {
        return repository.getOpinionsByPostId(id);
    }

    @Override
    public void addOpinion(User user, Post post, Long id) {
        Opinion opinion = opinionRepository.getById(id);
        post.getOpinions().put(user, opinion);
        repository.update(post);

    }

    @Override
    public int getPostsCount() {
        return repository.getPostsCount();
    }

    @Override
    public List<Post> findTenMostRecentCreatedPosts() {
        return repository.getAll().stream()
                .sorted(Comparator.comparing(Post::getCreateTime).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    @Override
    public List<Post> findTenMostRatedPosts() {
        return repository.getAll().stream()
                .sorted((p1, p2) -> Long.compare(p2.getRating(), p1.getRating()))
                .limit(10)
                .collect(Collectors.toList());
    }

    @Override
    public List<Post> findTenMostCommentedPosts() {
        return repository.getAll().stream()
                .sorted((p1, p2) -> Long.compare(p2.getComments().size(), p1.getComments().size()))
                .limit(10)
                .collect(Collectors.toList());
    }

    @Override
    public long getLikes(Post post) {
        try {
            return post.getOpinions().values().stream()
                    .filter(opinion -> opinion.getType().equals("LIKE")).count();
        } catch (NullPointerException e) {
            return 0;
        }
    }

    @Override
    public long getDislikes(Post post) {
        try {
            return post.getOpinions().values().stream()
                    .filter(opinion -> opinion.getType().equals("DISLIKE")).count();
        } catch (NullPointerException e) {
            return 0;
        }
    }
}