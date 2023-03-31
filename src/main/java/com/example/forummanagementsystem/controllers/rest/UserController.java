package com.example.forummanagementsystem.controllers.rest;
import com.example.forummanagementsystem.exceptions.AuthorizationException;
import com.example.forummanagementsystem.exceptions.EntityDuplicateException;
import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.helpers.AuthenticationHelper;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.models.dtos.UserDto;
import com.example.forummanagementsystem.services.mappers.UserMapper;
import com.example.forummanagementsystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    private final UserMapper modelMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public UserController(UserService userService, UserMapper modelMapper, AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<User> getUsers(@RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return userService.getAll(user);
        }catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id, @RequestHeader HttpHeaders headers) {
        try {
            authenticationHelper.tryGetUser(headers);
            return userService.get(id);
        }catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

    }

    @GetMapping("/name/{name}")
    public User getUserByName(@PathVariable String name, @RequestHeader HttpHeaders headers) {
        try {
            authenticationHelper.tryGetUser(headers);
            return userService.get(name);
        }catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

    }

    @GetMapping("/filter")
    public List<User> sort(
            @RequestHeader HttpHeaders headers,
            @RequestParam(required = false) Optional<String> name,
            @RequestParam(required = false) Optional<Integer> userId,
            @RequestParam(required = false) Optional<String> registeredTime,
            @RequestParam(required = false) Optional<Boolean> isAdmin,
            @RequestParam(required = false) Optional<Boolean> isBlocked,
            @RequestParam(required = false) Optional<String> sort
            ){

        try {
            authenticationHelper.tryGetUser(headers);
            return userService.filter(name, userId, registeredTime, isAdmin, isBlocked,sort);
        } catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

    }

    @PutMapping("/update/{id}")
    public User updateUser(@PathVariable Long id, @RequestHeader HttpHeaders headers,
                           @Valid @RequestBody UserDto userDto){
        try {
            authenticationHelper.tryGetUser(headers);
            User user = modelMapper.fromDto(userDto, id);
            userService.update(user);
            return user;
        }catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/changeIsAdmin/{to}/{username}")
    public User changeIsAdmin(@PathVariable String username, @PathVariable boolean to, @RequestHeader HttpHeaders headers){
        try {
            authenticationHelper.tryGetUser(headers);
            User user = userService.get(username);
            userService.changeIsAdmin(user, to);
            return user;
        } catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/changeIsBlocked/{to}/{username}")
    public User changeIsBlocked(@PathVariable String username, @PathVariable boolean to, @RequestHeader HttpHeaders headers){
        try {
            authenticationHelper.tryGetUser(headers);
            User user = userService.get(username);
            userService.changeIsBlocked(user, to);
            return user;
        } catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/create")
    public User createUser(@RequestHeader HttpHeaders headers,
                           @Valid @RequestBody UserDto userDto){
        try {
            authenticationHelper.tryGetUser(headers);
            User user = modelMapper.fromDto(userDto);
            userService.create(user);
            return user;
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @DeleteMapping("/delete/{username}")
    public User deleteUser(@PathVariable String username, @RequestHeader HttpHeaders headers){
        try {
            authenticationHelper.getLoggedInUser(headers);
            authenticationHelper.tryGetUser(headers);
            User user = userService.get(username);
            return userService.deleteUser(user);
        } catch (EntityNotFoundException e){
            throw new EntityNotFoundException("User", "username", username);
        }
    }



}
