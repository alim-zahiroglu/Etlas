//package com.etlas.service.impl;
//
//import com.etlas.dto.UserDto;
//import com.etlas.entity.User;
//import com.etlas.entity.common.CustomUserDetails;
//import com.etlas.repository.UserRepository;
//import com.etlas.service.SecurityService;
//import com.etlas.service.UserService;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//public class SecurityServiceImpl implements SecurityService {
//    private final UserRepository userRepository;
//    private final UserService userService;
//
//    public SecurityServiceImpl(UserRepository userRepository, @Lazy UserService userService) {
//        this.userRepository = userRepository;
//        this.userService = userService;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByUserName(username);
//        if (user==null) {
//            throw new UsernameNotFoundException("There is no user with this username");
//        }
//        return new CustomUserDetails(user);
//    }
//
//    @Override
//    public UserDto getLoggedInUser() {
//        var username = SecurityContextHolder.getContext().getAuthentication().getName();
//        return userService.findByUsername(username);
//    }
//}
