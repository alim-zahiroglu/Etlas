package com.etlas.controller;

import com.etlas.dto.UserDto;
import com.etlas.enums.Gender;
import com.etlas.enums.Role;
import com.etlas.enums.UserStatus;
import com.etlas.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String saveUser(@Valid @ModelAttribute("newUser") UserDto userDto, BindingResult bindingResult,
                           RedirectAttributes redirectAttributes, Model model){
        bindingResult = userService.validateNewUser(userDto,bindingResult);
        if (bindingResult.hasErrors()){
            userDto.setUseDefaultPassword(false);
            model.addAttribute("roles", Role.values());
            model.addAttribute("genders", Gender.values());
            model.addAttribute("userStatuses", UserStatus.values());
            return "user/user-create";
        }
        UserDto createdUser = userService.createUser(userDto);
        redirectAttributes.addFlashAttribute("userIsCreated", true);
        redirectAttributes.addFlashAttribute("createdUser", createdUser);
        return "redirect:/user/list";
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam("username") String username,
                             RedirectAttributes redirectAttributes){
        if (userService.isUserDeletable(username)) {
            UserDto deletedUser = userService.deleteUser(username);
            redirectAttributes.addFlashAttribute("userIsDeleted", true);
            redirectAttributes.addFlashAttribute("deletedUser", deletedUser);
            return "redirect:/user/list";
        }
        redirectAttributes.addFlashAttribute("userIsDeleted", false);
        redirectAttributes.addFlashAttribute("deleteMessage", "This is the only admin user, you can't delete it!");
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

    @PostMapping("/update/{id}")
    public String saveUpdatedUser(@Valid @ModelAttribute("userToBeUpdate") UserDto userToBeUpdate, BindingResult bindingResult,
                                  Model model, RedirectAttributes redirectAttributes){
        bindingResult = userService.validateUpdatedUser(userToBeUpdate,bindingResult);
        if (bindingResult.hasErrors()){
            userToBeUpdate.setUseDefaultPassword(false);
            userToBeUpdate.setUseCurrentPassword(false);
            model.addAttribute("roles", Role.values());
            model.addAttribute("genders", Gender.values());
            model.addAttribute("userStatuses", UserStatus.values());
            return "user/user-update";
        }
        UserDto updatedUser = userService.saveUpdatedUser(userToBeUpdate);
        redirectAttributes.addFlashAttribute("userIsUpdated", true);
        redirectAttributes.addFlashAttribute("updatedUser", updatedUser);
        return "redirect:/user/list";
    }
}
