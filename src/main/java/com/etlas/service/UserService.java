package com.etlas.service;
import com.etlas.dto.UserDto;
import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    List<UserDto> getUsers();

    UserDto findByUsername(String username);
    UserDto deleteUser(String username);

    List<UserDto> findAllUsers();
}
