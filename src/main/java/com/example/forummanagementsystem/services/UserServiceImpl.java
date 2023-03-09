package com.example.forummanagementsystem.services;

import com.example.forummanagementsystem.exceptions.AuthorizationException;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAll(User user) {
        if (!user.isAdmin()){
            throw new AuthorizationException("Only admin can see all the users!");
        }
        return userRepository.get();
    }

    @Override
    public User get(Long id) {
        return userRepository.get(id);
    }

    @Override
    public User get(String username) {
        return userRepository.get(username);
    }

    @Override
    public void update(User user) {
        userRepository.update(user);
    }

    @Override
    public void create(User user) {
        userRepository.createUser(user);
    }

    @Override
    public User deleteUser(User user) {
        return userRepository.delete(user);
    }

    @Override
    public List<User> filter(Optional<String> name,
                             Optional<Integer> userId,
                             Optional<String> registeredTime,
                             Optional<Boolean> isAdmin,
                             Optional<Boolean> isBlocked,
                             Optional<String> sort
    ) {
        LocalDateTime parsedRegisteredDate = null;
        if (registeredTime.isPresent()) {
            parsedRegisteredDate = LocalDateTime.parse(registeredTime.get());
        }
        return userRepository.filter(name, userId, Optional.ofNullable(parsedRegisteredDate), isAdmin, isBlocked, sort);
    }

    @Override
    public void changeIsAdmin(User user, boolean to) {
        userRepository.changeIsAdmin(user, to);
    }
}
