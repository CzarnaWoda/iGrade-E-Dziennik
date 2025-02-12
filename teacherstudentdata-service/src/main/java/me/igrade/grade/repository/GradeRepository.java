package me.igrade.grade.repository;

import me.igrade.grade.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade,Long> {


    List<Grade> getByStudentId(int studentId);

    List<Grade> getByTeacherId(int teacherId);

    Optional<Grade> getById(long id);

    void deleteById(long id);

    boolean existsById(long id);

    @Modifying
    @Query("UPDATE Grade g SET g.grade = :grade WHERE  g.id = :id")
    void updateGradeGradeByGradeId(@Param("id") long id, @Param("grade") int grade);
}
