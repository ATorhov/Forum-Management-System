package com.example.forummanagementsystem.services;



import com.example.forummanagementsystem.models.User;

import java.util.List;

public interface UserService {

    List<User> getAll(User user);

    User get(int id);

    User get(String username);

    void update(User user);
}
