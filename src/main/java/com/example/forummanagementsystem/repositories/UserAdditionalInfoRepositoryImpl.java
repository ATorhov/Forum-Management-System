package com.example.forummanagementsystem.repositories;

import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.models.UserAdditionalInfo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Repository;

@Repository
public class UserAdditionalInfoRepositoryImpl implements UserAdditionalInfoRepository {

    private final SessionFactory sessionFactory;

    public UserAdditionalInfoRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void create(UserAdditionalInfo uai) {
        try (Session session = sessionFactory.openSession()) {
            session.save(uai);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public void updateAdditionalUserInfo(UserAdditionalInfo userAdditionalInfo) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(userAdditionalInfo);
            session.getTransaction().commit();
        } catch (EntityNotFoundException e){
            throw new EntityNotFoundException("User", "username", userAdditionalInfo.getId().toString());
        }
    }
}
