package com.example.forummanagementsystem.repositories;





import com.example.forummanagementsystem.models.dtos.RegisterDto;
import com.example.forummanagementsystem.models.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    List<User> get();

    User get(Long id);

    User get(String username);

    User getByEmail(String email);

    void update(User user);

    void createUser(User user);

    User delete(User user);

    List<User> filter(Optional<String> name,
                      Optional<Integer> userId,
                      Optional<LocalDateTime> registeredTime,
                      Optional<Boolean> isAdmin,
                      Optional<Boolean> isBLocked,
                      Optional<String> sort
    );

    void changeIsAdmin(User user, boolean to);

    void changeIsBlocked(User user, boolean to);

    int getUsersCount();

    boolean userExists(RegisterDto register);
}
