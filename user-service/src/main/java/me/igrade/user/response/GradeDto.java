package me.igrade.user.response;


import lombok.Data;

import java.io.Serializable;

@Data
public class GradeDto implements Serializable {

    private int grade;
    private String teacherName;
    private String studentName;
    private String subjectName;

}
