package com.example.forummanagementsystem.repositories;

import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.models.CommentFilterOptions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

import com.example.forummanagementsystem.models.Comment;


@Repository
public class CommentRepositoryImpl implements CommentRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public CommentRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Comment> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Comment> list = session.createQuery(" from Comment ", Comment.class);
            return list.list();
        }
    }

    @Override
    public Comment getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Comment comment = session.get(Comment.class, id);
            if (comment == null) {
                throw new EntityNotFoundException("Comment", (long) id);
            }
            return comment;
        }
    }


    @Override
    public List<Comment> getCommentsByUserId(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Query<Comment> query = session.createQuery(
                    "SELECT c FROM Comment c WHERE c.user.id = :userId",
                    Comment.class
            );
            query.setParameter("userId", id);
            return query.list();
        }
    }

    @Override
    public void create(Comment comment) {
        try (Session session = sessionFactory.openSession()) {
            session.save(comment);
        }
    }

    @Override
    public void update(Comment comment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(comment);
            session.getTransaction().commit();
        }
    }


    @Override
    public void delete(int id) {
        Comment commentToDelete = getById(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(commentToDelete);
            session.getTransaction().commit();
        }
    }


    @Override
    public List<Comment> get(CommentFilterOptions commentFilterOptions) {


        return null;
    }

    @Override
    public List<Comment> filter(Optional<String> content, Optional<Integer> commentId, Optional<Integer> postId, Optional<Integer> userId, Optional<String> sort) {
        return null;
    }

    @Override
    public List<Comment> getCommentsFilterCommentOptions(CommentFilterOptions filterCommentOptions) {
        try (Session session = sessionFactory.openSession()) {
            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            filterCommentOptions.getContent().ifPresent(value -> {
                filters.add("content like :content");
                params.put("content", String.format("%%%s%%", value));
            });



            StringBuilder queryString = new StringBuilder("from Comment");
            if (!filters.isEmpty()) {
                queryString
                        .append(" where ")
                        .append(String.join(" and ", filters)
                        );
            }
            queryString.append(generateOrderBy(filterCommentOptions));

            Query<Comment> query = session.createQuery(queryString.toString(), Comment.class);
            query.setProperties(params);
            return query.list();
        }
    }

    private String generateOrderBy(CommentFilterOptions filterCommentOptions) {
        if (filterCommentOptions.getSortBy().isEmpty()) {
            return "";
        }

        String orderBy = "";
        switch (filterCommentOptions.getSortBy().get()) {
            case "content":
                orderBy = "content";
                break;
            case "commentId":
                orderBy = "commentId";
                break;
            default:
                return "";
        }

        orderBy = String.format(" order by %s", orderBy);

        if (filterCommentOptions.getSortOrder().isPresent() && filterCommentOptions.getSortOrder().get().equalsIgnoreCase("desc")) {
            orderBy = String.format("%s desc", orderBy);
        }

        return orderBy;
    }

    @Override
    public List<Comment> getAll(Optional<String> search) {
        if (search.isEmpty()) {
            return getAll();
        }
        try (Session session = sessionFactory.openSession()) {
            Query<Comment> list = session.createQuery(" from Comment where content like :content");
            list.setParameter("content", "%" + search.get() + "%");

            return list.list();
        }
    }

    private String generateStringFromSort(String value) {
        StringBuilder queryString = new StringBuilder(" order by ");
        String[] params = value.split("_");
        if (value.isEmpty()) {
            throw new UnsupportedOperationException(
                    "Sort should have max two params divided by _ symbol!");
        }
        switch (params[0]) {

            case "comment_id":
                queryString.append(" comment_id ");
                break;
            case "content":
                queryString.append(" content ");
                break;
            case "post_id":
                queryString.append(" post_id ");
                break;
            case "user_id":
                queryString.append(" user_id ");
                break;
        }
        if (params.length > 2) {
            throw new UnsupportedOperationException(
                    "Sort should have max two params divided by _ symbol!");
        }
        if (params.length == 2 && params[1].equalsIgnoreCase("desc")) {
            queryString.append(" desc ");
        }
        return queryString.toString();
    }
}
