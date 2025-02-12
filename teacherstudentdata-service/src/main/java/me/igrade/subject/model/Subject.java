package me.igrade.subject.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.igrade.grade.model.Grade;
import me.igrade.schoolclass.model.Class;

import java.util.List;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"subjectName", "class_id"})})
@Data
@NoArgsConstructor
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String subjectName;
    private int teacherId;
    @OneToOne
    @JoinColumn(name = "class_id", unique = true)
    private Class schoolClass;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
    private List<Grade> grades;


    public Subject(String subjectName, int teacherId, Class schoolClass){
        this.subjectName = subjectName;
        this.teacherId = teacherId;
        this.schoolClass = schoolClass;
    }
}
