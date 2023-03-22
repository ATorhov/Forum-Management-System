package com.example.forummanagementsystem.repositories;

import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.models.Opinion;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OpinionRepositoryImpl implements OpinionRepository{

    private final SessionFactory sessionFactory;

    @Autowired
    public OpinionRepositoryImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Opinion getById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Opinion opinion = session.get(Opinion.class, id);

            if (opinion == null) {
                throw new EntityNotFoundException("Opinion", id);
            }
            return opinion;
        }
    }
}
