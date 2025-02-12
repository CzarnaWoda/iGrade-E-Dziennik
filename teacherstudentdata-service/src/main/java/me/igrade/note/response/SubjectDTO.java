package me.igrade.note.response;

import lombok.Data;

import java.io.Serializable;


@Data
public class SubjectDTO implements Serializable {

    private String id;
    private String subjectName;
    private int teacherId;
    private String classId;
    private String className;



}
