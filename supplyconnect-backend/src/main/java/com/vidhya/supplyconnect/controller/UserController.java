package com.vidhya.supplyconnect.controller;

import com.vidhya.supplyconnect.dto.UserRequest;
import com.vidhya.supplyconnect.dto.UserResponse;
import com.vidhya.supplyconnect.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService=userService;
    }

    @PostMapping
    public UserResponse createUser(@Valid @RequestBody UserRequest request){
        return userService.saveUser(request);
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

}
