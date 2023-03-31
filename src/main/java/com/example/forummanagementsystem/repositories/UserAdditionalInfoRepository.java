package com.example.forummanagementsystem.repositories;

import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.models.UserAdditionalInfo;

public interface UserAdditionalInfoRepository {
    void create(UserAdditionalInfo uai);

    void updateAdditionalUserInfo(UserAdditionalInfo userAdditionalInfo);

    UserAdditionalInfo findByUser(User user1);
}
