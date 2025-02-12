package me.igrade.note.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class NoteDto implements Serializable {

    private int points;
    private String studentName;
    private String teacherName;
    private String description;
    private long id;

}
