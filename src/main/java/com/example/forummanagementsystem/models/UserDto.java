package com.example.forummanagementsystem.models;


import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

public class UserDto {



    @Size(min = 4, max = 32, message = "First name must be between 4 and 32 symbols!")
    private String firstName;
    @Size(min = 4, max = 32, message = "First name must be between 4 and 32 symbols!")
    private String lastName;
    @Email
    private String email;
    @Size(min = 4, max = 50, message = "Username must be between 4 and 50 symbols!")
    private String username;
    @Size(min = 4, max = 50, message = "Username must be at least 8 symbols containing letters and digits!")
    private String password;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
