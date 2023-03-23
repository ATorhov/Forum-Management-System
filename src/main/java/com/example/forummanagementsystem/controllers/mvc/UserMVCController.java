package com.example.forummanagementsystem.controllers.mvc;


import com.example.forummanagementsystem.helpers.AuthenticationHelper;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
public class UserMVCController {

    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;

    public UserMVCController(UserService userService, AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("isAdmin")
    public boolean isAdmin(HttpSession httpSession) {
        if (populateIsAuthenticated(httpSession)) {
            return userService.get(httpSession.getAttribute("currentUser").toString()).isAdmin();
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
        return "admin-dashboard";
    }

    @GetMapping("/user-details/{username}")
    public String getUserDetails(@PathVariable String username, Model model, HttpSession session){
        authenticationHelper.tryGetUser(session);
        User user = userService.get(username);
        model.addAttribute("user", userService.get(user.getUsername()));
        return "user-details-page";
    }
}
