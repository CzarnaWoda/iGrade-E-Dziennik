package me.igrade.subject.service;


import me.igrade.schoolclass.model.Class;
import me.igrade.subject.model.Subject;
import me.igrade.subject.request.CreateSubjectRequest;

import java.util.List;
import java.util.Optional;

public interface SubjectService {



    Subject createSubject(CreateSubjectRequest createSubjectRequest, Class c);
    Optional<Subject> getSubjectById(long subjectId);

    List<Subject> getSubjectsByClassId(int classId);

    List<Subject> getSubjectsByTeacherId(int teacherId);
    void deleteSubject(long subjectId);
}
