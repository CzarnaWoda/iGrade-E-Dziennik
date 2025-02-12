package me.igrade.grade.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.igrade.subject.model.Subject;

@Entity
@NoArgsConstructor
@Data
public class Grade {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private int grade;
    private int teacherId;
    private int studentId;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;
    private String teacherName;
    private String studentName;


    public Grade(int grade, int teacherId, int studentId, Subject subject, String teacherName, String studentName){
        this.grade = grade;
        this.teacherId = teacherId;
        this.subject = subject;
        this.studentId = studentId;
        this.teacherName = teacherName;
        this.studentName = studentName;
    }


}
