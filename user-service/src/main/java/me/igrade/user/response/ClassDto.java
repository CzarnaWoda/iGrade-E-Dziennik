package me.igrade.user.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class ClassDto implements Serializable {

    private long id;
    private String className;
    private int teacherId;

}
