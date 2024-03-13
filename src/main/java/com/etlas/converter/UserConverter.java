package com.etlas.converter;

import com.etlas.dto.UserDto;
import com.etlas.service.UserService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserConverter implements Converter<String, UserDto> {
    private final UserService userService;
    public UserConverter(UserService userService) {
        this.userService = userService;
    }
    @Override
    public UserDto convert(String userName) {
        if (userName.equals("")) {
            return null;
        }
        return userService.findByUsername(userName);
    }

}
