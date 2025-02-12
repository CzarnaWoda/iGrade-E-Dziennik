package me.igrade.user.response;

import lombok.Data;

import java.io.Serializable;


@Data
public class SubjectDTO implements Serializable {

    private long id;
    private String subjectName;
    private int teacherId;
    private long classId;
    private String className;


}
