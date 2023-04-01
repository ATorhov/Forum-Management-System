package com.example.forummanagementsystem.controllers.mvc;


import com.example.forummanagementsystem.exceptions.AuthorizationException;
import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.exceptions.UnauthorizerOperationException;
import com.example.forummanagementsystem.helpers.AuthenticationHelper;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.models.UserAdditionalInfo;
import com.example.forummanagementsystem.models.UserFilterOptions;
import com.example.forummanagementsystem.models.dtos.UserAdditionalInfoDto;
import com.example.forummanagementsystem.models.dtos.UserDto;
import com.example.forummanagementsystem.models.dtos.UserFilterDto;
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
import java.util.ArrayList;
import java.util.List;

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
        User user = null;
        try {
            user = authenticationHelper.tryGetUser(httpSession);
        } catch (AuthorizationException e) {

        }
        return user;
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
    public String getUsers(@ModelAttribute("userFilterOptions") UserFilterDto userFilterDto, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
            authenticationHelper.verifyIsAdmin(user);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizerOperationException e) {
            return "access_denied";
        }

        UserFilterOptions userFilterOptions = new UserFilterOptions(
                userFilterDto.getUsername()
        );

        model.addAttribute("users", userService.filter(userFilterOptions));
        model.addAttribute("userFilterDto", userFilterDto);
        return "all-users-page";
    }

    @PostMapping("/all")
    public String getUsers(Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
            authenticationHelper.verifyIsAdmin(user);
        } catch (AuthorizationException e) {
            return "redirect: /auth/login";
        } catch (UnauthorizerOperationException e) {
            return "access_denied";
        }
        List<User> users = userService.getAll(user);
        model.addAttribute("users", users);
        return "all-users-page";
    }

    @GetMapping("/admin/dashboard")
    public String getAdminDashboard(Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            authenticationHelper.verifyIsAdmin(user);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizerOperationException e) {
            return "access_denied";
        }
        model.addAttribute("usersCount", userService.getUsersCount());
        model.addAttribute("postsCount", postService.getPostsCount());

        return "admin-dashboard";
    }

    @GetMapping("/user-details/{username}")
    public String getUserDetails(@PathVariable String username, Model model, HttpSession session) {
        User user;
        try {
            authenticationHelper.tryGetUser(session);
            user = userService.get(username);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            return "error";
        }
        model.addAttribute("user", userService.get(user.getUsername()));
        model.addAttribute("postsCount", userService.getPostsCountToUser(user));
        return "user-details-page";
    }

    @GetMapping("/user/update/{username}")
    public String updateUserInfo(@PathVariable String username, HttpSession httpSession, Model model) {
        try {
            User user = authenticationHelper.tryGetUser(httpSession);
            if (!user.getUsername().equals(username)) {
                if (!user.isAdmin()) {
                    if (!user.isCreator()) {
                        throw new UnauthorizerOperationException("");
                    }
                }
            }
            if (user.isBlocked()){
                throw new UnauthorizerOperationException("");
            }
            model.addAttribute("userUpdate", new UserDto());
            model.addAttribute("userUpdateAdditionInfo", new UserAdditionalInfoDto());
            return "update-user-info-page";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizerOperationException e) {
            return "access_denied";
        } catch (EntityNotFoundException e) {
            return "error";
        }
    }

    @PostMapping("/user/update/{username}")
    public String handleUpdateUserInfo(@PathVariable String username, @ModelAttribute UserDto userUpdate,
                                       @ModelAttribute UserAdditionalInfoDto userUpdateAdditionInfo,
                                       HttpSession session) {
        try {
            User user1 = userMapper.fromDtoInfo(userUpdate, session);
            userService.update(user1);
            UserAdditionalInfo userAdditionalInfo = userMapper.userAdditionalInfoDtoToObject(userUpdateAdditionInfo);
            if (userAdditionalInfoService.findByUser(user1) == null) {
                userAdditionalInfo.setUser(user1);
                userAdditionalInfoService.create(userAdditionalInfo);
            } else {
                UserAdditionalInfo userAdditionalInfoFinal = userAdditionalInfoService.findByUser(user1);
                userAdditionalInfoFinal.setPhoneNumber(userAdditionalInfo.getPhoneNumber());
                userAdditionalInfoFinal.setAge(userAdditionalInfo.getAge());
                userAdditionalInfoFinal.setAddress(userAdditionalInfo.getAddress());
                userAdditionalInfoFinal.setBirthday(userAdditionalInfo.getBirthday());
                userAdditionalInfoFinal.setCountry(userAdditionalInfo.getCountry());
                userAdditionalInfoFinal.setProfession(userAdditionalInfo.getProfession());
                userAdditionalInfoFinal.setDescribeProfession(userAdditionalInfo.getDescribeProfession());
                userAdditionalInfoService.updateAdditionalUserInfo(userAdditionalInfoFinal);
            }
        } catch (EntityNotFoundException e) {
            return "error";
        }

        return "redirect:/users/user-details/" + username;
    }

    @GetMapping("/user/{username}/changeRole/{to}")
    public String changeIsAdmin(@PathVariable String username, @PathVariable boolean to, HttpSession httpSession) {
        try {
            User userCheck = authenticationHelper.tryGetUser(httpSession);
            if (!userCheck.isAdmin()) {
                throw new UnauthorizerOperationException("Get out");
            }
            authenticationHelper.tryGetUser(httpSession);
            User user = userService.get(username);
            userService.changeIsAdmin(user, to);
            return "redirect:/users/user-details/" + username;
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizerOperationException e) {
            return "access_denied";
        } catch (EntityNotFoundException e) {
            return "error";
        }
    }

    @GetMapping("user/{username}/changeStatus/{to}")
    public String changeStatus(@PathVariable String username, @PathVariable boolean to, HttpSession session) {
        try {
            User userCheck = authenticationHelper.tryGetUser(session);
            if (!userCheck.isAdmin()) {
                throw new UnauthorizerOperationException("Get out");
            }
            authenticationHelper.tryGetUser(session);
            User user = userService.get(username);
            userService.changeIsBlocked(user, to);
            return "redirect:/users/user-details/" + username;
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizerOperationException e) {
            return "access_denied";
        } catch (EntityNotFoundException e) {
            return "error";
        }
    }


}
