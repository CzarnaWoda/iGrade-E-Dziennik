package me.igrade.user.response;

import lombok.Data;

@Data
public class GradeTeacherDto {


    private int grade;
    private String teacherName;
    private String studentName;
    private String subjectName;

    private long id;
}
