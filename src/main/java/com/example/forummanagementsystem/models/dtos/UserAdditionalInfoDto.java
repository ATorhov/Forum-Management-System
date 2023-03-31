package com.example.forummanagementsystem.models.dtos;

import com.example.forummanagementsystem.models.User;
import org.springframework.stereotype.Repository;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class UserAdditionalInfoDto {


//    @Size(min = 6, max = 10, message = "Phone number must be not less than 6 and no more than 10 symbols.")
    private String phoneNumber;

    private User user;


    private String profession;

    private String describeProfession;

    private String country;

    private int age;

    private String address;

    private String birthday;


    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getDescribeProfession() {
        return describeProfession;
    }

    public void setDescribeProfession(String describeProfession) {
        this.describeProfession = describeProfession;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

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
