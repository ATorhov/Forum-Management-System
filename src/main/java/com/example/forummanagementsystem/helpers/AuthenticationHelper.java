package com.example.forummanagementsystem.helpers;

import com.example.forummanagementsystem.exceptions.AuthorizationException;
import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.services.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AuthenticationHelper {
    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String INVALID_AUTHENTICATION_ERROR = "Invalid authentication.";
    private static final String ERROR_MESSAGE = "Permission denied, only admin is authorized for this operation.";
    private final UserService userService;

    public AuthenticationHelper(UserService userService) {
        this.userService = userService;
    }


    public User tryGetUser(HttpHeaders headers) {
        if (!headers.containsKey(AUTHORIZATION_HEADER_NAME)) {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }

        try {
            String userInfo = headers.getFirst(AUTHORIZATION_HEADER_NAME);
            String username = getUsername(userInfo);
            String password = getPassword(userInfo);
            User user = userService.get(username);

            if (!user.getPassword().equals(password)) {
                throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
            }

            return user;
        } catch (EntityNotFoundException e) {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }
    }

    private String getUsername(String userInfo) {
        int firstSpace = userInfo.indexOf(" ");
        if (firstSpace == -1) {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }

        return userInfo.substring(0, firstSpace);
    }

    private String getPassword(String userInfo) {
        int firstSpace = userInfo.indexOf(" ");
        if (firstSpace == -1) {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }
        return userInfo.substring(firstSpace + 1);
    }

    public void checkPermissions(Long targetUserId, User userToCheck) {
        if (!userToCheck.isAdmin() && userToCheck.getId() != targetUserId) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ERROR_MESSAGE);
        }
    }


}
