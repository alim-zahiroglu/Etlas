package com.etlas.service;

import com.etlas.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface SecurityService extends UserDetailsService {
    UserDto getLoggedInUser();
}
