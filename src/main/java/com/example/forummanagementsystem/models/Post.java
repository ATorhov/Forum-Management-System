package com.example.forummanagementsystem.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.time.LocalDateTime;
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
    private int rating;

    @Column(columnDefinition = "TEXT")
    private String content;

    // @JsonIgnore
    @Column(name = "create_time", updatable = false)
    @CreationTimestamp
    private LocalDateTime createTime;

    // @JsonIgnore
    @Column(name = "update_time")
    @UpdateTimestamp
    private LocalDateTime updateTime;

    @ManyToOne
    // @JsonIgnore
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(fetch = FetchType.EAGER)
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

    public int getRating() {
        return rating;
    }

    // TODO delete setRating once likes/dislikes behaviour is implemented.
    public void setRating(int rating) {
        this.rating = rating;
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

    public int getLikes(){
        int likes = 0;
        likes = (int) opinions.values().stream()
                .filter(opinion -> opinion.getType().equals("like")).count();
        return likes;
    }

    public int getDislikes(){
        int dislikes = 0;
        dislikes = (int) opinions.values().stream()
                .filter(opinion -> opinion.getType().equals("dislike")).count();
        return dislikes;
    }

    public void setUser(@NonNull User user) {
        this.user = user;
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

    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", title='" + title + '\'' +
                ", rating=" + rating +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", user=" + user +
                ", comments=" + comments +
                ", opinions=" + opinions +
                '}';
    }
}