package me.igrade.note.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private int points;
    private int studentId;
    private int teacherId;
    private String studentName;
    private String teacherName;

    private String description;

    public Note(int points, int studentId, int teacherId, String description, String studentName, String teacherName){
        this.points = points;
        this.studentId = studentId;
        this.teacherId = teacherId;
        this.description = description;

        this.studentName = studentName;
        this.teacherName = teacherName;
    }

}
