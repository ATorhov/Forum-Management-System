package com.example.forummanagementsystem.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @Column(name = "title")
    private String title;

    @Column(name = "rating")
    private long rating;

    @Column(columnDefinition = "TEXT")
    private String content;

     @JsonIgnore
    @Column(name = "create_time", updatable = false)
    @CreationTimestamp
    private LocalDateTime createTime;

     @JsonIgnore
    @Column(name = "update_time")
    @UpdateTimestamp
    private LocalDateTime updateTime;

    @ManyToOne
     @JsonIgnore
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "post_opinions",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "opinion_id"))
    @MapKeyJoinColumn(name = "user_id")
    private Map<User, Opinion> opinions;

    public Post() {
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getRating() {
        return rating;
    }


    public void setRealRating() {
        this.rating = getLikes() - getDislikes();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @NonNull
    public User getUser() {
        return user;
    }

    public Map<User, Opinion> getOpinions() {
        return opinions;
    }

    public void setOpinions(Map<User, Opinion> opinions) {
        this.opinions = opinions;
    }

    @JsonIgnore
    public long getLikes() {
        try {
            return opinions.values().stream()
                    .filter(opinion -> opinion.getType().equals("LIKE")).count();
        } catch (NullPointerException e) {
            return 0;
        }
    }

    @JsonIgnore
    public long getDislikes(){
        try {
            return opinions.values().stream()
                    .filter(opinion -> opinion.getType().equals("DISLIKE")).count();
        } catch (NullPointerException e){
            return 0;
        }
    }

    public void setUser(@NonNull User user) {
        this.user = user;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @JsonIgnore
    public String getFormattedCreateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd 'at' HH:mm");
        return createTime.format(formatter);
    }

    @JsonIgnore
    public String getFormattedUpdateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd 'at' HH:mm");
        return updateTime.format(formatter);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return postId == post.postId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(postId);
    }
}