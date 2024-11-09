package com.g1.mychess.admin.mapper;

import com.g1.mychess.admin.dto.UserDTO;
import com.g1.mychess.admin.model.Admin;

public class AdminMapper {

    public static UserDTO toUserDTO(Admin admin) {
        return new UserDTO(
                admin.getAdminId(),
                admin.getUsername(),
                admin.getPassword(),
                admin.getEmail(),
                admin.getRole()
        );
    }
}