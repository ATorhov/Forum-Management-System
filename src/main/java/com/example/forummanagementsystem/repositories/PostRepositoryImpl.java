package com.example.forummanagementsystem.repositories;

import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
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
}