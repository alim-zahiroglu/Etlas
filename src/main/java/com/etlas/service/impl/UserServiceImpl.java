package com.etlas.service.impl;

import com.etlas.dto.UserDto;
import com.etlas.entity.User;
import com.etlas.mapper.MapperUtil;
import com.etlas.repository.UserRepository;
import com.etlas.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final MapperUtil mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setPassWord(passwordEncoder.encode(userDto.getPassWord()));
        User savedUser = repository.save(mapper.convert(userDto,new User()));
        return mapper.convert(savedUser,new UserDto());
    }

    @Override
    public List<UserDto> getUsers() {
        List<User> users = repository.findAll();
        return users.stream()
                .map(user -> mapper.convert(user, new UserDto()))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findByUsername(String username) {
        User foundUser  = repository.findByUserName(username);
        return mapper.convert(foundUser,new UserDto());
    }
}
