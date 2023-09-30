package com.taldate.backend.user.controller;

import com.taldate.backend.user.dto.UserDTO;
import com.taldate.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping("/user/password/{id}")
    public UserDTO updatePassword(@PathVariable Integer id, @RequestBody UserDTO userDTO) {
        return userService.updatePassword(id, userDTO);
    }

    @PutMapping("/user/email/{id}")
    public UserDTO updateEmail(@PathVariable Integer id, @RequestBody UserDTO userDTO) {
        return userService.updateEmail(id, userDTO);
    }

    @PutMapping("/user/name/{id}")
    public UserDTO updateName(@PathVariable Integer id, @RequestBody UserDTO userDTO) {
        return userService.updateName(id, userDTO);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUserByID(id);
        return ResponseEntity.noContent().build();
    }

}
