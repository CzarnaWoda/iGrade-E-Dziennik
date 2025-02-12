package me.igrade.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class StudentDTO implements Serializable {
    private String firstName;
    private String lastName;

    private String email;

    private long classId;



}
