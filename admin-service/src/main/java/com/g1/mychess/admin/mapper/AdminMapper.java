package com.g1.mychess.admin.mapper;

import com.g1.mychess.admin.dto.UserDTO;
import com.g1.mychess.admin.model.Admin;

/**
 * A utility class for mapping between `Admin` model and `UserDTO` objects.
 * Provides methods for converting `Admin` entities to `UserDTO` representations.
 */
public class AdminMapper {

    /**
     * Converts an `Admin` object to a `UserDTO`.
     *
     * @param admin the `Admin` object to be converted
     * @return the corresponding `UserDTO` object
     */
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