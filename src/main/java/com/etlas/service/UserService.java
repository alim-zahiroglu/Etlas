package com.etlas.service;
import com.etlas.dto.UserDto;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    List<UserDto> getUsers();

    UserDto findByUsername(String username);
    UserDto deleteUser(String username);

    List<UserDto> findAllUsers();

    UserDto getUserByUserName(String userName);

    UserDto saveUpdatedUser(UserDto updatedUser);

    BindingResult validateUpdatedUser(UserDto userToBeUpdate, BindingResult bindingResult);

    UserDto findById(long id);

    BindingResult validateNewUser(UserDto userDto, BindingResult bindingResult);

    boolean isUserDeletable(String username);
}
