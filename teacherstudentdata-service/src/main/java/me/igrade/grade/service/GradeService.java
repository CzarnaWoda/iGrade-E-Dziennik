package me.igrade.grade.service;

import me.igrade.events.NotificationCreateEvent;
import me.igrade.events.enums.NotificationReceiverType;
import me.igrade.events.enums.NotificationType;
import me.igrade.grade.dto.GradeDto;
import me.igrade.grade.model.Grade;
import me.igrade.response.StudentDTO;
import me.igrade.response.TeacherDTO;
import me.igrade.subject.model.Subject;

import java.util.List;
import java.util.Optional;

public interface GradeService {

    List<GradeDto> getGradesByStudentId(int studentId);

    List<GradeDto> getGradesByTeacherId(int teacherId);
    Optional<Grade> getGradeById(long gradeId);
    boolean existById(long gradeId);

    boolean deleteGrade(long gradeId);


    boolean createGrade(int grade, int teacherId, int studentId, long subjectId);
}
