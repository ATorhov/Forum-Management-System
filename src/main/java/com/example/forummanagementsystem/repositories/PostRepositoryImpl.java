package com.example.forummanagementsystem.repositories;

import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.models.Post;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class PostRepositoryImpl implements PostRepository{

    private final SessionFactory sessionFactory;

    @Autowired
    public PostRepositoryImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Post> getAll() {
        try(Session session = sessionFactory.openSession()) {
            TypedQuery<Post> query = session.createQuery("from Post", Post.class);
            return query.getResultList();
        }
    }


    @Override
    public Post getById(int id) {
        try(Session session = sessionFactory.openSession()) {
            Post post = session.get(Post.class, id);
            if (post == null){
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
    public void delete(int id) {
        Post postToDelete = getById(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(postToDelete);
            session.getTransaction().commit();
        }
    }
}
