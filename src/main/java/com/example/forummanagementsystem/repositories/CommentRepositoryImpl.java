package com.example.forummanagementsystem.repositories;

import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

//import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.*;

import com.example.forummanagementsystem.models.Comment;


@Repository
public class CommentRepositoryImpl implements CommentRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public CommentRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

//    @Override
//    public List<Comment> getAll() {
//        try (Session session = sessionFactory.openSession()) {
//            TypedQuery<Comment> query = session.createQuery("from Comment", Comment.class);
//            return query.getResultList();
//        }
//    }

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
    public void update(Comment comment, User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(comment);
            session.update(user);
            session.getTransaction();
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
    public List<Comment> filter(Optional<String> content,
                                Optional<Integer> commentId,
                                Optional<Integer> postId,
                                Optional<Integer> userId,
                                Optional<String> sort) {
        try (
                Session session = sessionFactory.openSession()) {
            StringBuilder queryString = new StringBuilder(" from Comment ");

            Map<String, Object> queryParams = new HashMap<>();
            ArrayList<String> filter = new ArrayList<>();


            content.ifPresent(value -> {
                filter.add(" content like:content");
                queryParams.put("content", "%" + value + "%");
            });

            commentId.ifPresent(value -> {
                filter.add(" commentId = :commentId");
                queryParams.put("commentId", value);
            });


            postId.ifPresent(value -> {
                filter.add(" postId = :postId");
                queryParams.put("postId", value);
            });

            userId.ifPresent(value -> {
                filter.add(" userId = :userId");
                queryParams.put("userId", value);
            });


            if (!filter.isEmpty()) {
                queryString.append(" where ").append(String.join(" and ", filter));
            }

            sort.ifPresent(value -> queryString.append(generateStringFromSort(value)));

            Query<Comment> queryList = session.createQuery(queryString.toString(), Comment.class);
            queryList.setProperties(queryParams);

            System.out.println(queryString);

            return queryList.list();
        }
    }

    @Override
    public List<Comment> getAll(Optional<String> search) {

        try (Session session = sessionFactory.openSession()) {
            Query<Comment> list = session.createQuery(" from Comment where content like :content or commentId = :commentId");
            list.setParameter("content", "%" + search.get() + "%");
            list.setParameter("commentId", getNumberIfPresent(search));

            return list.list();
        }
    }

    private Integer getNumberIfPresent(Optional<String> search) {
        try {
            return Integer.parseInt(search.get());
        } catch (NumberFormatException e) {
            return -1;
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


        if (params.length > 3) {
            throw new UnsupportedOperationException(
                    "Sort should have max two params divided by _ symbol!");
        }
        if (params.length == 3 && params[1].equalsIgnoreCase("desc")) {
            queryString.append("desc");
        }

        return queryString.toString();

    }

//    public List<Comment> filter(List<Comment> comments, Integer comment_id, String content, Integer post_id, Integer user_id) {
//
//        comments = filterByCommentId(comments, comment_id);
//        comments = filterByContent(comments, content);
//        comments = filterByPostId(comments, post_id);
//        comments = filterByUserId(comments, user_id);
//
//        return comments;
//    }
}
