package me.igrade.user.model;


import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "grade_users")
@NoArgsConstructor
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String firstName;
    private String lastName;
    private boolean enabled;
    private String password;
    private String email;
    private Long classId;
    @OneToOne
    private UserRole role;

    public boolean isStudent(){
        return role != null && role.getName().equalsIgnoreCase("STUDENT");
    }

    @PrePersist
    @PreUpdate
    public void validateClassIdForStudent(){
        if(isStudent() && classId == null){
            throw new IllegalStateException("Student doesn't have classId");
        }
    }


    public User(String firstName, String lastName, boolean enabled, String password, String email,Long classId, UserRole role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.enabled = enabled;
        this.password = password;
        this.email = email;
        this.classId = classId;
        this.role = role;
    }
}
