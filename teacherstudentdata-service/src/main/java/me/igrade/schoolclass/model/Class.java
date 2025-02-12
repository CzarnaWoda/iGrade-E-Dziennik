package me.igrade.schoolclass.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Class {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String className;
    private int teacherId;
    private String classCode;


    public Class(String className, int teacherId, String classCode){
        this.className = className;
        this.teacherId = teacherId;

        this.classCode = classCode;


    }

}
