package com.example.forummanagementsystem.repositories;

import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.models.UserAdditionalInfo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    @Override
    public UserAdditionalInfo findByUser(User user1) {
        try (Session session = sessionFactory.openSession()) {
            Query<UserAdditionalInfo> query = session.createQuery("from UserAdditionalInfo where user = :user1", UserAdditionalInfo.class);
            query.setParameter("user1", user1);

            List<UserAdditionalInfo> result = query.list();
            if (result.size() == 0) {
                return null;
            }


            return result.get(0);
        }
    }
}
