package com.g1.mychess.auth.util;

import com.g1.mychess.auth.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsFactory {
    public UserDetails createUserDetails(UserDTO userDTO, String role) {
        return new org.springframework.security.core.userdetails.User(
                userDTO.getUsername(),
                userDTO.getPassword(),
                Collections.singleton(new org.springframework.security.core.authority.SimpleGrantedAuthority(role))
        );
    }
}
