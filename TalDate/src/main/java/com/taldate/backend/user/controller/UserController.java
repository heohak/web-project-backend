package com.taldate.backend.user.controller;

import com.taldate.backend.user.dto.UserDTO;
import com.taldate.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping()
    public List<UserDTO> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @PostMapping("/register")
    public UserDTO registerUser(@RequestBody UserDTO userDTO) {
        return userService.register(userDTO);
    }

    @PutMapping("/password/{id}")
    public UserDTO updatePassword(@PathVariable Integer id, @RequestBody UserDTO userDTO) {
        return userService.updatePassword(id, userDTO);
    }

    @PutMapping("/email/{id}")
    public UserDTO updateEmail(@PathVariable Integer id, @RequestBody UserDTO userDTO) {
        return userService.updateEmail(id, userDTO);
    }

    @PutMapping("/name/{id}")
    public UserDTO updateName(@PathVariable Integer id, @RequestBody UserDTO userDTO) {
        return userService.updateName(id, userDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUserByID(id);
        return ResponseEntity.noContent().build();
    }

}
