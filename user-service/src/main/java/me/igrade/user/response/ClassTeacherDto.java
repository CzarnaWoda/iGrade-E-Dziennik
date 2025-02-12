package me.igrade.user.response;


import lombok.Data;

@Data
public class ClassTeacherDto {

    private long id;

    private String className;
    private int teacherId;
    private String classCode;


}
