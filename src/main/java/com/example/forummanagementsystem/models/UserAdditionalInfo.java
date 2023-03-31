package com.example.forummanagementsystem.models;

import javax.persistence.*;

@Entity
@Table(name = "user_additional_info")
public class UserAdditionalInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "phone_number_id")
    private Long id;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "profession")
    private String profession;

    @Column(name = "described_profession")
    private String describeProfession;

    @Column(name = "country")
    private String country;

    @Column(name = "age")
    private int age;

    @Column(name = "address")
    private String address;

    @Column(name = "birthday")
    private String birthday;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

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

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}