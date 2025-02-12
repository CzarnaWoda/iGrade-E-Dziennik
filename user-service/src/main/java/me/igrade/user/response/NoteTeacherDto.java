package me.igrade.user.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class NoteTeacherDto implements Serializable {

    private int points;
    private String studentName;
    private String teacherName;
    private String description;

    private long id;
}
