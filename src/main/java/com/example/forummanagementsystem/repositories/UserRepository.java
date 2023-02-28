package com.example.forummanagementsystem.repositories;





import com.example.forummanagementsystem.models.User;

import java.util.List;

public interface UserRepository {

    List<User> get();

    User get(Long id);

    User get(String username);

    void update(User user);

    void createUser(User user);
}
