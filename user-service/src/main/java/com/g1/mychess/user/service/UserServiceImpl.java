package com.g1.mychess.user.service;

import com.g1.mychess.user.repository.*;
import com.g1.mychess.user.model.*;

import java.util.Objects;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    public UserServiceRepository userServiceRepository;

    @Override
    public List<User> listUsers() {
        return userServiceRepository.findAll();
    }

    @Override
    public User getUser(Long id) {
        for (User user : userServiceRepository.findAll()) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public User addUser(User user) {
        userServiceRepository.save(user);
        return userServiceRepository.save(user);
    }

    @Override
    public User updateUser(User user, Long id) {
        User uService = userServiceRepository.findById(id).get();

        // Updates field if they are not null or empty
        if (Objects.nonNull(uService.getUsername()) && !"".equalsIgnoreCase(user.getUsername())) {
            uService.setUsername(user.getUsername());
        }
        if (Objects.nonNull(uService.getEmail()) && !"".equalsIgnoreCase(user.getEmail())) {
            uService.setEmail(user.getEmail());
        }
        if (Objects.nonNull(uService.getPassword()) && !"".equalsIgnoreCase(user.getPassword())) {
            user.setPassword(user.getPassword());
        }
        if (Objects.nonNull(user.getRole()) && !user.getRole().equals(uService.getRole())) {
            uService.setRole(user.getRole());
        }
        // Saves and returns the updated department entity.
        return userServiceRepository.save(uService);
    }

    public User deleteUser(Long id) {
        User deletedUser = getUser(id);
        userServiceRepository.deleteById(id);

        return deletedUser;
    }
}
