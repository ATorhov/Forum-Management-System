package com.example.forummanagementsystem.controllers.mvc;


import com.example.forummanagementsystem.exceptions.AuthorizationException;
import com.example.forummanagementsystem.exceptions.EntityDuplicateException;
import com.example.forummanagementsystem.helpers.AuthenticationHelper;
import com.example.forummanagementsystem.models.*;
import com.example.forummanagementsystem.services.UserAdditionalInfoService;
import com.example.forummanagementsystem.services.UserService;
import com.example.forummanagementsystem.services.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthenticationMvcController {

    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;
    private final UserAdditionalInfoService userAdditionalInfoService;

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


    @Autowired
    public AuthenticationMvcController(UserService userService,
                                       AuthenticationHelper authenticationHelper,
                                       UserMapper userMapper, UserAdditionalInfoService userAdditionalInfoService) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;
        this.userAdditionalInfoService = userAdditionalInfoService;
    }
    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("login", new LoginDto());
        return "login-view";
    }

    @PostMapping("/login")
    public String handleLogin(@Valid @ModelAttribute("login") LoginDto login,
                              BindingResult bindingResult,
                              HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "login-view";
        }

        try {
            authenticationHelper.verifyAuthentication(login.getUsername(), login.getPassword());
            session.setAttribute("currentUser", login.getUsername());
            return "redirect:/home";
        } catch (AuthorizationException e) {
            bindingResult.rejectValue("username", "auth_error", e.getMessage());
            return "login-view";
        }
    }

    @GetMapping("/logout")
    public String handleLogout(HttpSession session) {
        session.removeAttribute("currentUser");
        return "redirect:/";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("phoneNumber", new UserAdditionalInfoDto());
        model.addAttribute("register", new RegisterDto());
        return "register-view";
    }

    @PostMapping("/register")
    public String handleRegister(@Valid @ModelAttribute("register") RegisterDto register,
                                 @Valid @ModelAttribute("phoneNumber") UserAdditionalInfoDto userAdditionalInfoDto ,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "register-view";
        }

        if (!register.getPassword().equals(register.getPasswordConfirm())) {
//            bindingResult.rejectValue("passwordConfirm", "password_error", "Password confirmation should match password.");
            redirectAttributes.addFlashAttribute("register", register);
            redirectAttributes.addFlashAttribute("notConfirmed", true);
            return "redirect:/auth/register";
        }

        if (userService.userExists(register)){
            redirectAttributes.addFlashAttribute("register", register);
            redirectAttributes.addFlashAttribute("userExistsError", true);

            return "redirect:/auth/register";
        }

        try {
            User user = userMapper.fromDto(register);
            userService.create(user);
            if (!userAdditionalInfoDto.getPhoneNumber().isEmpty()) {
                userAdditionalInfoDto.setUser(user);
                UserAdditionalInfo uai = userMapper.userAdditionalInfoDtoToObject(userAdditionalInfoDto,
                        new UserAdditionalInfo());
                userAdditionalInfoService.create(uai);
            }
            return "redirect:/auth/login";
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("username", "username_error", e.getMessage());
            return "register-view";
        }
    }



}
