package com.g1.mychess.tournament.service;

import com.g1.mychess.tournament.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final WebClient.Builder webClientBuilder;

    public CustomUserDetailsService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO userDTO = webClientBuilder.build()
                .get()
                .uri("http://user-service:8081/api/v1/users/username/" + username)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block();

        if (userDTO == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return new org.springframework.security.core.userdetails.User(
                userDTO.getUsername(),
                userDTO.getPassword(),
                Collections.singleton(new org.springframework.security.core.authority.SimpleGrantedAuthority(userDTO.getRole()))
        );
    }
}
