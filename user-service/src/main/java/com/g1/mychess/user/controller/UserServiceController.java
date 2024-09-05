package com.g1.mychess.user.controller;

import com.g1.mychess.user.service.*;
import com.g1.mychess.user.model.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserServiceController {

    @Autowired
    private UserService userService;

    public UserServiceController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.listUsers();
    }
        
    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id) {
        User user = userService.getUser(id);

        if(user == null) {
            throw new UserNotFoundException(id);
        }

        return user;
    }
    
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users")
    public User updateUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        User user1 = userService.getUser(id);

        if (user1 == null) {
            throw new UserNotFoundException(id);
        }

        return userService.updateUser(user1, id);
    }
    
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        User user2 = userService.getUser(id);

        if (user2 == null) {
            throw new UserNotFoundException(id);
        }

        userService.deleteUser(id);
    }    
}
