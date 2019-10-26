package com.example.demo.service;

import com.example.demo.DTO.display.UserRegistrationDto;
import com.example.demo.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    UserEntity findUserByUsername(String username);

    UserEntity findUserByEmail(String email);

    List<UserEntity> findAllUser();

    void delete(Long id);

    UserEntity saveUserDto(UserRegistrationDto userDTO);

    void editStatusUser(Long id);

    void editRoleUser(Long id, String roleName);

    void updatePassword(String password, Long userId);

    UserEntity getAuthUser();

    UserEntity getUser();

    Boolean existsByUserName (String userName);

}
