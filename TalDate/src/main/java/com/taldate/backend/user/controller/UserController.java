package com.taldate.backend.user.controller;

import com.taldate.backend.user.dto.UserDTO;
import com.taldate.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/user")
    public List<UserDTO> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/user/{id}")
    public UserDTO getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }


}
