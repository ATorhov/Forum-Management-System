package com.example.forummanagementsystem.services;

import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.models.UserAdditionalInfo;

public interface UserAdditionalInfoService {
    void create(UserAdditionalInfo uai);

    void updateAdditionalUserInfo(UserAdditionalInfo userAdditionalInfo);

    UserAdditionalInfo findByUser(User user1);
}
