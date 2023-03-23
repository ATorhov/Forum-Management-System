package com.example.forummanagementsystem.helpers;

import com.example.forummanagementsystem.exceptions.AuthorizationException;
import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.exceptions.UnauthorizerOperationException;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.services.UserService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.servlet.http.HttpSession;

@Component
public class AuthenticationHelper {
    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String INVALID_AUTHENTICATION_ERROR = "Invalid authentication.";
    private static final String ERROR_MESSAGE = "Permission denied, only admin is authorized for this operation.";
    private final UserService userService;
    private final SessionFactory sessionFactory;


    public AuthenticationHelper(UserService userService, SessionFactory sessionFactory) {
        this.userService = userService;
        this.sessionFactory = sessionFactory;
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

    public void checkAccessPermissions(Long targetUserId, User executingUser) {
        if (!executingUser.isAdmin() && executingUser.getId() != targetUserId) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ERROR_MESSAGE);
        }
    }



    public User getLoggedInUser(HttpHeaders headers) {
        Session session = sessionFactory.openSession(); // Get the session, but don't create one if it doesn't exist
        if (session != null) {
            return (User) session.getTransaction(); // Retrieve the user from the session
        } else {
            throw new AuthorizationException("You are not logged in"); // No session, no logged in user
        }
    }

    public User verifyAuthentication(String username, String password) {
        try {
            User user = userService.get(username);
            if (user.getPassword().equals(password)){
                return user;
            }
            if (!BCrypt.checkpw(password, user.getPassword())) {
                throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
            }
            return user;
        } catch (EntityNotFoundException e) {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }
    }

    public User tryGetUser(HttpSession session) {
        String currentUsername = (String) session.getAttribute("currentUser");
        if (currentUsername == null) {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }
        return userService.get(currentUsername);
    }

    public void verifyIsAdmin(User user) {
        if (!user.isAdmin()){
            throw new UnauthorizerOperationException
                    ("You are not admin and shouldn't be even trying to open this page!!");
        }
    }
}
