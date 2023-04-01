package com.example.forummanagementsystem.models;

import java.util.Optional;

public class UserFilterOptions {

    private Optional<String> username;

    public UserFilterOptions(String username) {
        this.username = Optional.ofNullable(username);
    }

    public Optional<String> getUsername() {
        return username;
    }

    public void setUsername(Optional<String> username) {
        this.username = username;
    }
}
