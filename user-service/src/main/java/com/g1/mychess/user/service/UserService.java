package com.g1.mychess.user.service;

import com.g1.mychess.user.model.*;
import java.util.List;

public interface UserService {
    List<User> listUsers();
    User getUser(Long id);
    User addUser(User user);
    User updateUser(User user, Long id);
    User deleteUser(Long id);
}
