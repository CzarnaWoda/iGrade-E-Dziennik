package me.igrade.schoolclass.repository;

import me.igrade.schoolclass.model.Class;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassRepository extends JpaRepository<Class,Long> {


    List<Class> getClassesByTeacherId(int teacherId);

    Optional<Class> getClassById(long classId);

    Optional<Class> getClassByClassName(String className);

    boolean existsByClassName(String className);

    Optional<Class> getClassByClassCode(String classCode);

    boolean existsByClassCode(String classCode);

    boolean existsById(long id);

    void deleteById(long id);

}
