package com.example.forummanagementsystem.repositories;

import com.example.forummanagementsystem.exceptions.AlreadyHasThisBooleanException;
import com.example.forummanagementsystem.exceptions.EntityDuplicateException;
import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.models.UserFilterOptions;
import com.example.forummanagementsystem.models.dtos.RegisterDto;
import com.example.forummanagementsystem.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import org.hibernate.query.Query;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SessionFactory sessionFactory;

    public UserRepositoryImpl(JdbcTemplate jdbcTemplate, SessionFactory sessionFactory) {
        this.jdbcTemplate = jdbcTemplate;
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
    public User getByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where email =:email", User.class);
            query.setParameter("email", email);

            List<User> result = query.list();
            if (result.size() == 0) {
                throw new EntityNotFoundException("User", "email", email);
            }
            return result.get(0);
        }
    }

    @Override
    public void update(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
//            user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
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
                String salt = BCrypt.gensalt();
                String hashedPassword = BCrypt.hashpw(user.getPassword(), salt);
                user.setPassword(hashedPassword);
                user.setRegisteredTime(LocalDateTime.now());
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

    @Override
    public List<User> filter(Optional<String> name,
                             Optional<Integer> userId,
                             Optional<LocalDateTime> registeredTime,
                             Optional<Boolean> isAdmin,
                             Optional<Boolean> isBlocked,
                             Optional<String> sort
    ) {
        try(Session session = sessionFactory.openSession()){
            StringBuilder queryString = new StringBuilder(" from User ");

            Map<String, Object> queryParams = new HashMap<>();
            ArrayList<String> filter = new ArrayList<>();

            name.ifPresent(v -> {
                filter.add(" username like :name ");
                queryParams.put("name", "%" + v + "%");
            });

            userId.ifPresent(value -> {
                filter.add(" user_id = :userId ");
                queryParams.put("userId", value);
            });

            registeredTime.ifPresent(v ->{
                filter.add(" registeredTime = :registeredTime");
                queryParams.put("registeredTime", v);
            });

            isAdmin.ifPresent(v -> {
                filter.add(" isAdmin = :isAdmin");
                queryParams.put("isAdmin", v);
            });

            isBlocked.ifPresent(v ->{
                filter.add(" isBlocked = :isBlocked");
                queryParams.put("isBlocked", v);
            });

            if (!filter.isEmpty()){
                queryString.append(" where ").append(String.join(" and ", filter));
            }

            sort.ifPresent(v -> {
                queryString.append(generateStringFromSort(v));
            });

            Query<User> queryList = session.createQuery(queryString.toString(), User.class);
            queryList.setProperties(queryParams);

            return queryList.list();
        }
    }


    @Override
    public List<User> filter(UserFilterOptions userFilterOptions) {
        try(Session session = sessionFactory.openSession()){
            StringBuilder queryString = new StringBuilder(" from User ");

            Map<String, Object> queryParams = new HashMap<>();
            ArrayList<String> filter = new ArrayList<>();

            userFilterOptions.getUsername().ifPresent(v -> {
                filter.add(" username like :username ");
                queryParams.put("username", "%" + v + "%");
            });

            if (!filter.isEmpty()){
                queryString.append(" where ").append(String.join(" and ", filter));
            }

            Query<User> queryList = session.createQuery(queryString.toString(), User.class);
            queryList.setProperties(queryParams);

            return queryList.list();
        }
    }

    @Override
    public void changeIsAdmin(User user, boolean to) {
        try (Session session = sessionFactory.openSession()) {
            if (to == user.isAdmin()){
                if (to){
                    throw new AlreadyHasThisBooleanException(String.format("%s is already an admin!",
                            user.getUsername()));
                } else {
                    throw new AlreadyHasThisBooleanException(String.format("%s is already not an admin",
                            user.getUsername()));
                }
            }
            session.beginTransaction();
            user.setAdmin(to);
            session.update(user);
            session.getTransaction().commit();
        } catch (EntityNotFoundException e){
            throw new EntityNotFoundException("User", "username", user.getUsername());
        }
    }

    @Override
    public void changeIsBlocked(User user, boolean to) {
        try (Session session = sessionFactory.openSession()) {
            if (to == user.isBlocked()){
                if (!to){
                    throw new AlreadyHasThisBooleanException(String.format("%s is already not blocked",
                            user.getUsername()));
                } else {
                    throw new AlreadyHasThisBooleanException(String.format("%s is already blocked",
                            user.getUsername()));
                }
            }
            session.beginTransaction();
            user.setBlocked(to);
            session.update(user);
            session.getTransaction().commit();
        } catch (EntityNotFoundException e){
            throw new EntityNotFoundException("User", "username", user.getUsername());
        }
    }

    private String generateStringFromSort(String v) {
        StringBuilder queryString = new StringBuilder(" order by ");
        String[] params = v.split("_");

        if (v.isEmpty()){
            return "";
        }

        switch (params[0]) {
            case "username":
                queryString.append(" username ");
                break;
            case "registeredTime":
                queryString.append(" registration_date ");
                break;
            case "userId":
                queryString.append(" user_id ");
                break;
            default:
                throw new UnsupportedOperationException(
                        "Sort should have max two params divided by _ symbol!");
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

    public int getUsersCount() {
        try {
            String sql = "SELECT COUNT(*) FROM users";
            return jdbcTemplate.queryForObject(sql, Integer.class);
        } catch (NullPointerException e){
            throw new NullPointerException(e.getMessage());
        }
    }

    @Override
    public boolean userExists(RegisterDto register) {
        boolean exists;
        try {
            get(register.getUsername());
            exists = true;
        } catch (EntityNotFoundException e){
            exists = false;
        }
        return exists;
    }

    @Override
    public int getPostsCountToUser(User user) {
        try {
            String sql = "SELECT COUNT(*) FROM posts WHERE user_id = ?";
            return jdbcTemplate.queryForObject(sql, Integer.class, user.getId());
        } catch (NullPointerException e){
            throw new NullPointerException(e.getMessage());
        }
    }
}
