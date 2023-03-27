package com.example.forummanagementsystem.services;

import com.example.forummanagementsystem.models.UserAdditionalInfo;
import com.example.forummanagementsystem.repositories.UserAdditionalInfoRepository;
import org.springframework.stereotype.Service;

@Service
public class UserAdditionalInfoServiceImpl implements UserAdditionalInfoService {

    private final UserAdditionalInfoRepository userAdditionalInfoRepository;

    public UserAdditionalInfoServiceImpl(UserAdditionalInfoRepository userAdditionalInfoRepository) {
        this.userAdditionalInfoRepository = userAdditionalInfoRepository;
    }

    @Override
    public void create(UserAdditionalInfo uai) {
        userAdditionalInfoRepository.create(uai);
    }
}
