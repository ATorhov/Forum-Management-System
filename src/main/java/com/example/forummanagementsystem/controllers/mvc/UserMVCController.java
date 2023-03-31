package com.example.forummanagementsystem.controllers.mvc;


import com.example.forummanagementsystem.exceptions.AuthorizationException;
import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.helpers.AuthenticationHelper;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.models.UserAdditionalInfo;
import com.example.forummanagementsystem.models.dtos.UserAdditionalInfoDto;
import com.example.forummanagementsystem.models.dtos.UserDto;
import com.example.forummanagementsystem.services.PostService;
import com.example.forummanagementsystem.services.UserAdditionalInfoService;
import com.example.forummanagementsystem.services.UserService;
import com.example.forummanagementsystem.services.mappers.UserMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
public class UserMVCController {

    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final PostService postService;
    private final UserMapper userMapper;

    private final UserAdditionalInfoService userAdditionalInfoService;

    public UserMVCController(UserService userService, AuthenticationHelper authenticationHelper, PostService postService, UserMapper userMapper, UserAdditionalInfoService userAdditionalInfoService) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.postService = postService;
        this.userMapper = userMapper;
        this.userAdditionalInfoService = userAdditionalInfoService;
    }

    @ModelAttribute("user")
    public User getUser(HttpSession httpSession) {
        return authenticationHelper.tryGetUser(httpSession);
    }

    @ModelAttribute("isAdmin")
    public boolean isAdmin(HttpSession httpSession) {
        if (populateIsAuthenticated(httpSession)) {
            return userService.get(httpSession.getAttribute("currentUser").toString()).isAdmin();
        } else {
            return false;
        }
    }

    @ModelAttribute("isCreator")
    public boolean isCreator(HttpSession httpSession) {
        if (populateIsAuthenticated(httpSession)) {
            return userService.get(httpSession.getAttribute("currentUser").toString()).isCreator();
        } else {
            return false;
        }
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }


    @GetMapping("/all")
    public String getUsers(Model model, HttpSession session){
        User user = authenticationHelper.tryGetUser(session);
        model.addAttribute("users", userService.getAll(user));
        return "all-users-page";
    }

    @GetMapping("/admin/dashboard")
    public String getAdminDashboard(Model model, HttpSession session){
        User user = authenticationHelper.tryGetUser(session);
        authenticationHelper.verifyIsAdmin(user);
        model.addAttribute("usersCount", userService.getUsersCount());
        model.addAttribute("postsCount", postService.getPostsCount());

        return "admin-dashboard";
    }

    @GetMapping("/user-details/{username}")
    public String getUserDetails(@PathVariable String username, Model model, HttpSession session){
        authenticationHelper.tryGetUser(session);
        User user = userService.get(username);
        model.addAttribute("user", userService.get(user.getUsername()));
        return "user-details-page";
    }

    @GetMapping("/user/update/{username}")
    public String updateUserInfo(@PathVariable String username, HttpSession httpSession, Model model){
        try {
            User user = authenticationHelper.tryGetUser(httpSession);
            if (!user.getUsername().equals(username)){
                throw new AuthorizationException("You cannot update another user info!");
            }
            model.addAttribute("userUpdate", new UserDto());
            model.addAttribute("userUpdateAdditionInfo", new UserAdditionalInfoDto());
            return "update-user-info-page";
        } catch (AuthorizationException e){
//            throw new AuthorizationException(e.getMessage());
            return "access_denied";
        }
    }

    @PostMapping("/user/update/{username}")
    public String handleUpdateUserInfo(@PathVariable String username, @ModelAttribute UserDto userUpdate,
                                       @ModelAttribute UserAdditionalInfoDto userUpdateAdditionInfo,
                                       HttpSession session){
        try {
            User userCheck = authenticationHelper.tryGetUser(session);
            if (!userCheck.isAdmin() || !userCheck.isCreator()){
                return "access_denied";
            }
            User user = userService.get(username);
            userUpdateAdditionInfo.setUser(user);
            User user1 = userMapper.fromDtoInfo(userUpdate, session);
            userService.update(user1);
            UserAdditionalInfo userAdditionalInfo = userMapper.userAdditionalInfoDtoToObject(userUpdateAdditionInfo);
            userAdditionalInfoService.updateAdditionalUserInfo(userAdditionalInfo);
        } catch (EntityNotFoundException e){
            throw new EntityNotFoundException(userUpdateAdditionInfo.getUser().getUsername(),
                    userUpdateAdditionInfo.getUser().getFirstName(), userUpdateAdditionInfo.getUser().getLastName());
        }

        return "redirect:/user-details/" + username;
    }

    @GetMapping("/user/{username}/changeRole/{to}")
    public String changeIsAdmin(@PathVariable String username, @PathVariable boolean to, HttpSession httpSession){
        try {
            User userCheck = authenticationHelper.tryGetUser(httpSession);
            if (!userCheck.isAdmin() || !userCheck.isCreator()){
                return "access_denied";
            }
            authenticationHelper.tryGetUser(httpSession);
            User user = userService.get(username);
            userService.changeIsAdmin(user, to);
            return "redirect:/users/user-details/" + username;
        } catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("user/{username}/changeStatus/{to}")
    public String changeStatus(@PathVariable String username, @PathVariable boolean to, HttpSession session){
        try {
            User userCheck = authenticationHelper.tryGetUser(session);
            if (!userCheck.isAdmin() || !userCheck.isCreator()){
                return "access_denied";
            }
            authenticationHelper.tryGetUser(session);
            User user = userService.get(username);
            userService.changeIsBlocked(user, to);
            return "redirect:/users/user-details/" + username;
        } catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
