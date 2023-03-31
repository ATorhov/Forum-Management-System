package com.example.forummanagementsystem.models.dtos;

import com.example.forummanagementsystem.models.User;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class UserAdditionalInfoDto {


//    @Size(min = 6, max = 10, message = "Phone number must be not less than 6 and no more than 10 symbols.")
    private String phoneNumber;

    private User user;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
