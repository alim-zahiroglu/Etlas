package com.etlas.service;

import com.etlas.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public interface SecurityService extends UserDetailsService {
    UserDto getLoggedInUser();
}
