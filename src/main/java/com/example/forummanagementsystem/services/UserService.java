package com.example.forummanagementsystem.services;



import com.example.forummanagementsystem.models.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAll(User user);

    User get(Long id);

    User get(String username);

    void update(User user);
    void create(User user);

    User deleteUser(User user);

    List<User> filter(Optional<String> name,
                      Optional<Integer> userId,
                      Optional<String> registeredTime,
                      Optional<String> sort
    );
}
