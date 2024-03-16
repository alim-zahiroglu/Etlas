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
    public UserDto convert(String userId) {
        if (userId.equals("")) {
            return null;
        }
        long id = Long.parseLong(userId);
        return userService.findById(id);
    }

}
