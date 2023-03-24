package com.example.forummanagementsystem.repositories;

import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.models.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public PostRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Post> getAll() {
        try (Session session = sessionFactory.openSession()) {
            TypedQuery<Post> query = session.createQuery("from Post", Post.class);
            return query.getResultList();
        }
    }

    // for MVC filtering only;
    @Override
    public List<Post> get(PostFilterOptions filterOptions) {
        try (Session session = sessionFactory.openSession()) {
            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            filterOptions.getTitle().ifPresent(value -> {
                filters.add("title like :title");
                params.put("title", String.format("%%%s%%", value));
            });

            filterOptions.getContent().ifPresent(value -> {
                filters.add("content like :content");
                params.put("content", String.format("%%%s%%", value));
            });

            filterOptions.getContent().ifPresent(value -> {
                filters.add("rating >= :minRating");
                params.put("rating", value);
            });

            filterOptions.getContent().ifPresent(value -> {
                filters.add("createDate >= :startDate");
                params.put("createDate", value);
            });

            filterOptions.getContent().ifPresent(value -> {
                filters.add("updateDate >= :updateDate");
                params.put("updateDate", value);
            });

            StringBuilder queryString = new StringBuilder("from Post");
            if (!filters.isEmpty()) {
                queryString
                        .append(" where ")
                        .append(String.join(" and ", filters));
            }
            queryString.append(generateOrderBy(filterOptions));

            Query<Post> query = session.createQuery(queryString.toString(), Post.class);
            query.setProperties(params);
            return query.list();
        }
    }

    @Override
    public List<Post> search(Optional<String> all) {
        if (!all.isPresent()) {
            return Collections.emptyList(); // Return an empty list if the optional value is not present
        }

        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("from Post where title like :title or content like :content");
            query.setParameter("title", "%" + all.get() + "%");
            query.setParameter("content", "%" + all.get() + "%");
            List<Post> result = query.list(); // Execute the query to obtain the result list
            return result;
        }
    }

    @Override
    public List<Comment> getCommentsByPostId(Long id) {
        try (Session session = sessionFactory.openSession()) {
            TypedQuery<Comment> query = session.createQuery(
                    "SELECT c FROM Comment c WHERE c.post.id = :postId",
                    Comment.class
            );
            query.setParameter("postId", id);
            return query.getResultList();
        }
    }

    @Override
    public Post getById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Post post = session.get(Post.class, id);
            if (post == null) {
                throw new EntityNotFoundException("Post", id);
            }
            return post;
        }
    }

    @Override
    public Post getByTitle(String title) {
        try (Session session = sessionFactory.openSession()) {
            TypedQuery<Post> query = session.createQuery("from Post where title = :title", Post.class);
            query.setParameter("title", title);
            List<Post> result = query.getResultList();
            if (result.size() == 0) {
                throw new EntityNotFoundException("Post", "title", title);
            }
            return result.get(0);
        }
    }


    @Override
    public void create(Post post) {
        try (Session session = sessionFactory.openSession()) {
            session.save(post);
        }
    }

    @Override
    public void update(Post post) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(post);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Post post, User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(post);
            session.update(user);
            session.getTransaction();
        }
    }


    @Override
    public void delete(Long id) {
        Post postToDelete = getById(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(postToDelete);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Post> getPostsByUserId(Long userId) {
        try (Session session = sessionFactory.openSession()) {
            TypedQuery<Post> query = session.createQuery("FROM Post WHERE user.id = :userId", Post.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        }
    }

    @Override
    public List<Post> filter(Optional<String> title,
                             Optional<String> content,
                             Optional<Integer> rating,
                             Optional<LocalDateTime> createDateTime,
                             Optional<LocalDateTime> updateDateTime,
                             Optional<String> sort) {

        try (Session session = sessionFactory.openSession()) {
            StringBuilder queryString = new StringBuilder(" from Post ");

            Map<String, Object> queryParams = new HashMap<>();
            ArrayList<String> filter = new ArrayList<>();

            title.ifPresent(value -> {
                filter.add(" title like :title");
                queryParams.put("title", "%" + value + "%");
            });

            content.ifPresent(value -> {
                filter.add(" content like :content");
                queryParams.put("content", "%" + value + "%");
            });

            rating.ifPresent(value -> {
                filter.add(" rating = :rating");
                queryParams.put("rating", value);
            });

            createDateTime.ifPresent(value -> {
                filter.add(" createTime = :createTime");
                queryParams.put("createTime", value);
            });

            updateDateTime.ifPresent(value -> {
                filter.add(" updateTime = :updateTime");
                queryParams.put("updateTime", value);
            });

            if (!filter.isEmpty()) {
                queryString.append(" where ").append(String.join(" and ", filter));
            }

            sort.ifPresent(value -> queryString.append(getGeneratedStringFromSort(value)));

            Query<Post> queryList = session.createQuery(queryString.toString(), Post.class);
            queryList.setProperties(queryParams);

            return queryList.list();
        }
    }

    private String getGeneratedStringFromSort(String value) {
        StringBuilder queryString = new StringBuilder(" order by ");
        String[] params = value.split("_");

        if (value.isEmpty()) {
            throw new UnsupportedOperationException("Sort should have max two params divided by _ symbol.");
        }
        switch (params[0]) {
            case "title":
                queryString.append(" title ");
                break;
            case "content":
                queryString.append(" content ");
                break;
            case "rating":
                queryString.append(" rating ");
                break;
            default:
                throw new UnsupportedOperationException("Sort should have max two params divided by _ symbol.");
        }
        if (params.length > 2) {
            throw new UnsupportedOperationException("Operation cannot be proceed.");
        }
        if (params.length == 2 && params[1].equalsIgnoreCase("desc")){
            queryString.append(" desc ");
        }
        return queryString.toString();
    }

    private String generateOrderBy(PostFilterOptions filterOptions) {
        if (filterOptions.getSortBy().isEmpty()) {
            return "";
        }

        String orderBy = "";
        switch (filterOptions.getSortBy().get()) {
            case "title":
                orderBy = "title";
                break;
            case "content":
                orderBy = "content";
                break;
            case "rating":
                orderBy = "rating";
                break;
            case "createDate":
                orderBy = "createDate";
                break;
            case "updateDate":
                orderBy = "updateDate";
                break;
        }

        orderBy = String.format(" order by %s", orderBy);

        if (filterOptions.getSortOrder().isPresent() && filterOptions.getSortOrder().get().equalsIgnoreCase("desc")) {
            orderBy = String.format("%s desc", orderBy);
        }
        return orderBy;
    }
}