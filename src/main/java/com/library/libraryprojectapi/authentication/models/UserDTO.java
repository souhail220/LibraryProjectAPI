package com.library.libraryprojectapi.authentication.models;

import com.library.libraryprojectapi.data.models.RoleName;
import lombok.Data;

@Data
public class UserDTO {

    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private RoleName role;

    public UserDTO(){}

    public UserDTO(long id, String firstName, String lastName, String email, String phoneNumber, RoleName role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }
}
