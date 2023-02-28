package com.example.forummanagementsystem.repositories;

import com.example.forummanagementsystem.models.Comment;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import com.example.forummanagementsystem.exceptions.CommentNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.models.Post;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;


@Repository
public class CommentRepositoryImpl implements CommentRepository {

    // private List<Comment> comments;
    private final SessionFactory sessionFactory;

    @Autowired
    public CommentRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;

    }

//    @Override
//    public List<Comment> getAll() {
//        try (Session session = sessionFactory.openSession()) {
//            Query<Comment> query = session.createQuery("from Comment", Comment.class);
//            return query.list();
//        }
//    }

    @Override
    public List<Comment> getAll() {
        try(Session session = sessionFactory.openSession()) {
            Query<Comment> query = session.createQuery("from Comment", Comment.class);
            return query.getResultList();
        }
    }



    @Override
    public Comment getById(int id) {
        try(Session session = sessionFactory.openSession()) {
            Comment comment = session.get(Comment.class, id);
            if (comment == null){
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
    public void delete(int id) {
        Comment commentToDelete = getById(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(commentToDelete);
            session.getTransaction().commit();
        }
    }
}
