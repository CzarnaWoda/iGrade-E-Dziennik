package me.igrade.subject.repository;

import me.igrade.subject.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface SubjectRepository extends JpaRepository<Subject,Long> {

    Optional<Subject> getSubjectById(long subjectId);

    List<Subject> getSubjectsBySchoolClassId(long classId);

    List<Subject> getSubjectsByTeacherId(int classId);

    void deleteById(long subjectId);

}
