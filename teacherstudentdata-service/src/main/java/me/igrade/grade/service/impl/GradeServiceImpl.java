package me.igrade.grade.service.impl;


import lombok.RequiredArgsConstructor;
import me.igrade.events.NotificationCreateEvent;
import me.igrade.events.enums.NotificationReceiverType;
import me.igrade.events.enums.NotificationType;
import me.igrade.grade.dto.GradeDto;
import me.igrade.grade.mapper.GradeMapper;
import me.igrade.grade.model.Grade;
import me.igrade.grade.repository.GradeRepository;
import me.igrade.grade.service.GradeService;
import me.igrade.response.StudentDTO;
import me.igrade.response.TeacherDTO;
import me.igrade.subject.model.Subject;
import me.igrade.subject.service.SubjectService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@Service

@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {

    private final GradeRepository gradeRepository;

    private final GradeMapper gradeMapper;

    private final WebClient.Builder webClientBuilder;

    private final KafkaTemplate<String, NotificationCreateEvent> kafkaTemplate;

    private final SubjectService subjectService;


    @Override
    public List<GradeDto> getGradesByStudentId(int studentId){
        return gradeRepository.getByStudentId(studentId).stream().map(gradeMapper::mapGradeToGradeDto).toList();
    }

    @Override
    public List<GradeDto> getGradesByTeacherId(int teacherId){
        return gradeRepository.getByTeacherId(teacherId).stream().map(gradeMapper::mapGradeToGradeDto).toList();
    }

    @Override
    public Optional<Grade> getGradeById(long gradeId){
        return gradeRepository.getById(gradeId);
    }

    @Override
    public boolean existById(long gradeId){
        return gradeRepository.existsById(gradeId);
    }

    @Override
    public boolean deleteGrade(long gradeId){
        gradeRepository.deleteById(gradeId);
        return true;
    }


    @Override
    public boolean createGrade(int grade, int teacherId, int studentId, long subjectId){
        //Example of sync communication

        StudentDTO responseStudent = webClientBuilder.build().get()
                .uri("http://user-service/api/v1/student/id/" + studentId)
                .retrieve()
                .bodyToMono(StudentDTO.class)
                .block();
        TeacherDTO responseTeacher = webClientBuilder.build().get()
                .uri("http://user-service/api/v1/teacher/id/" + teacherId)
                .retrieve()
                .bodyToMono(TeacherDTO.class)
                .block();


            if(responseStudent == null || responseTeacher == null){
                return false;
            }

            final Optional<Subject> optionalSubject = subjectService.getSubjectById(subjectId);
            if(optionalSubject.isPresent()) {
                if (responseStudent.getClassId() != optionalSubject.get().getSchoolClass().getId()) {
                    return false;
                }
            }else{
                return false;
            }

            final Grade newGrade = new Grade(grade, teacherId, studentId, optionalSubject.get(), responseTeacher.getFirstName() + " " + responseTeacher.getLastName(), responseStudent.getFirstName() + " " +  responseStudent.getLastName());

            gradeRepository.save(newGrade);

            kafkaTemplate.send("notificationCreateTopic", new NotificationCreateEvent(NotificationReceiverType.STUDENT, NotificationType.GRADE, "Nowa ocena została wpisana na twoje konto studenckie", studentId));

            return true;
        }
}
