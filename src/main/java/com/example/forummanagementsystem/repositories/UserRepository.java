package com.example.forummanagementsystem.repositories;





import com.example.forummanagementsystem.models.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    List<User> get();

    User get(Long id);

    User get(String username);

    void update(User user);

    void createUser(User user);

    User delete(User user);

    List<User> filter(Optional<String> name,
                      Optional<Integer> userId,
                      Optional<LocalDateTime> registeredTime,
                      Optional<String> sort
    );
}
