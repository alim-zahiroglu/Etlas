package com.etlas.controller;

import com.etlas.dto.UserDto;
import com.etlas.enums.Gender;
import com.etlas.enums.Role;
import com.etlas.enums.UserStatus;
import com.etlas.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/list")
    public String getAllUsers(Model model){
        model.addAttribute("userList", userService.findAllUsers());
        return "user/user-list";
    }

    @GetMapping("/create")
    public String userCreate(Model model){
        model.addAttribute("newUser",new UserDto());
        model.addAttribute("roles", Role.values());
        model.addAttribute("genders", Gender.values());
        model.addAttribute("userStatuses", UserStatus.values());
        return "/user/user-create";
    }
    @PostMapping("/create")
    public String saveUser(@ModelAttribute("userDto") UserDto userDto,
                           RedirectAttributes redirectAttributes){
        System.out.println(userDto);
        UserDto createdUser = userService.createUser(userDto);
        redirectAttributes.addFlashAttribute("userIsCreated", true);
        redirectAttributes.addFlashAttribute("createdUser", createdUser);
        return "redirect:/user/list";
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam("username") String username,
                             RedirectAttributes redirectAttributes){
        UserDto deletedUser = userService.deleteUser(username);
        redirectAttributes.addFlashAttribute("userIsDeleted",true);
        redirectAttributes.addFlashAttribute("deletedUser", deletedUser);
        return "redirect:/user/list";
    }

    @GetMapping("/update/{userName}")
    public String updateUser(@PathVariable("userName") String userName, Model model){
        UserDto userToBeUpdate = userService.getUserByUserName(userName);
        model.addAttribute("userToBeUpdate",userToBeUpdate);
        model.addAttribute("roles", Role.values());
        model.addAttribute("genders", Gender.values());
        model.addAttribute("userStatuses", UserStatus.values());
        return "user/user-update";
    }

    @PostMapping("/update")
    public String saveUpdatedUser(@ModelAttribute("updatedUser") UserDto updatedUser,
                           RedirectAttributes redirectAttributes){
        System.out.println(updatedUser);
        UserDto createdUser = userService.saveUpdatedUser(updatedUser);
        redirectAttributes.addFlashAttribute("userIsUpdated", true);
        redirectAttributes.addFlashAttribute("updatedUser", createdUser);
        return "redirect:/user/list";
    }
}
