package me.igrade.user.dto;

import lombok.Data;

@Data
public class UserDto {

    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private long classId;
    private String role;



}
