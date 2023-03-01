package com.example.forummanagementsystem.repositories;

import com.example.forummanagementsystem.exceptions.EntityDuplicateException;
import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Repository;

import org.hibernate.query.Query;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final SessionFactory sessionFactory;

    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public List<User> get() {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User", User.class);
            return query.list();
        }
    }

    @Override
    public User get(Long id) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, id);
            if (user == null) {
                throw new EntityNotFoundException("User", id);
            }
            return user;
        }
    }

    @Override
    public User get(String username) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where username = :username", User.class);
            query.setParameter("username", username);

            List<User> result = query.list();
            if (result.size() == 0) {
                throw new EntityNotFoundException("User", "username", username);
            }

            return result.get(0);
        }
    }

    @Override
    public void update(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
            session.update(user);
            session.getTransaction().commit();
        } catch (EntityNotFoundException e){
            throw new EntityNotFoundException("User", "username", user.getUsername());
        }
    }

    @Override
    public void createUser(User user) {
        boolean alreadyExists;
        try {
            get(user.getUsername());
            alreadyExists = true;
        }catch (EntityNotFoundException e){
            alreadyExists = false;
        }

        if (!alreadyExists) {
            try (Session session = sessionFactory.openSession()) {
                String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
                user.setPassword(hashedPassword);
                user.setLocalDateTime(LocalDateTime.now());
                session.save(user);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        } else {
            throw new EntityDuplicateException("User", "username", user.getUsername());
        }
    }

    @Override
    public User delete(User user) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.delete(user);
            session.getTransaction().commit();
        }
        return user;
    }
}
