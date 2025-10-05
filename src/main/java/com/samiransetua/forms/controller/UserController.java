package com.samiransetua.forms.controller;

import com.samiransetua.forms.entity.User;
import com.samiransetua.forms.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;
    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user){
        return userService.createUser(user);
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Long id,@RequestBody User updatedUser){
        return userService.updateUser(id, updatedUser);
    }

    @PostMapping("/login")
    public String login(@RequestBody User loginRequest){
        return  userService.findByEmail(loginRequest.getEmail()).filter(user ->
            user.getPassword().equals(loginRequest.getPassword())
        ).map(user -> "Login Successfull").orElse("Invalid email or password");
    }

}
