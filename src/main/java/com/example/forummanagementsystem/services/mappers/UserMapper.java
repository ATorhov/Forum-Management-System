package com.example.forummanagementsystem.services.mappers;

import com.example.forummanagementsystem.helpers.AuthenticationHelper;
import com.example.forummanagementsystem.models.*;
import com.example.forummanagementsystem.models.dtos.RegisterDto;
import com.example.forummanagementsystem.models.dtos.UserAdditionalInfoDto;
import com.example.forummanagementsystem.models.dtos.UserDto;
import com.example.forummanagementsystem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Component
public class UserMapper {

    private final UserRepository userRepository;


    private final AuthenticationHelper authenticationHelper;
    @Autowired
    public UserMapper(UserRepository userRepository, AuthenticationHelper authenticationHelper) {
        this.userRepository = userRepository;
        this.authenticationHelper = authenticationHelper;
    }


    public User fromDto(UserDto userDto) {
        User user = new User();
        dtoToObject(userDto, user);
        return user;
    }

    public User fromDtoInfo(UserDto userDto, HttpSession session) {
        User user = authenticationHelper.tryGetUser(session);
        dtoToObject(userDto, user);
        return user;
    }

    public User fromDto(UserDto userDto, Long id) {
        User user = userRepository.get(id);
        dtoToObject(userDto, user);
        return user;
    }

    public User fromDto(RegisterDto registerDto) {
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(registerDto.getPassword());
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        return user;
    }

    private void dtoToObject(UserDto userDto, User user) {
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
//        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
//        user.setUsername(userDto.getUsername());
    }

    public UserAdditionalInfo userAdditionalInfoDtoToObject(UserAdditionalInfoDto userAdditionalInfoDto) {
        UserAdditionalInfo userAdditionalInfo = new UserAdditionalInfo();
        userAdditionalInfo.setPhoneNumber(userAdditionalInfoDto.getPhoneNumber());
        userAdditionalInfo.setUser(userAdditionalInfoDto.getUser());
        userAdditionalInfo.setAge(userAdditionalInfoDto.getAge());
        userAdditionalInfo.setAddress(userAdditionalInfoDto.getAddress());
        userAdditionalInfo.setBirthday(userAdditionalInfoDto.getBirthday());
        userAdditionalInfo.setCountry(userAdditionalInfoDto.getCountry());
        userAdditionalInfo.setProfession(userAdditionalInfoDto.getProfession());
        userAdditionalInfo.setDescribeProfession(userAdditionalInfoDto.getDescribeProfession());
        return userAdditionalInfo;
    }

    public UserAdditionalInfo userAdditionalInfoDtoToObject(UserAdditionalInfoDto userAdditionalInfoDto,
                                                            UserAdditionalInfo userAdditionalInfo) {
        userAdditionalInfo.setPhoneNumber(userAdditionalInfoDto.getPhoneNumber());
        userAdditionalInfo.setUser(userAdditionalInfoDto.getUser());
        return userAdditionalInfo;
    }

}
