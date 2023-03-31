package com.example.forummanagementsystem.services.mappers;

import com.example.forummanagementsystem.models.*;
import com.example.forummanagementsystem.models.dtos.RegisterDto;
import com.example.forummanagementsystem.models.dtos.UserAdditionalInfoDto;
import com.example.forummanagementsystem.models.dtos.UserDto;
import com.example.forummanagementsystem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final UserRepository userRepository;

    @Autowired
    public UserMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User fromDto(UserDto userDto) {
        User user = new User();
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
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
    }

    public UserAdditionalInfo userAdditionalInfoDtoToObject(UserAdditionalInfoDto userAdditionalInfoDto,
                                                            UserAdditionalInfo userAdditionalInfo){
        userAdditionalInfo.setPhoneNumber(userAdditionalInfoDto.getPhoneNumber());
        userAdditionalInfo.setUser(userAdditionalInfoDto.getUser());
        return userAdditionalInfo;
    }

}
