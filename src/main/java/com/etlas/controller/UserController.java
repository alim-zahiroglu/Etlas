package com.etlas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
public class UserController {
    @GetMapping("/delete")
    public String deleteUser(@RequestParam("username") String username, Model model){
        // TODO deletion operation
        model.addAttribute("deleted_user","success");
        return "/user/user-list";
    }
}
