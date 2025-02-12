package me.igrade.subject.service.impl;


import lombok.RequiredArgsConstructor;
import me.igrade.events.NotificationCreateEvent;
import me.igrade.events.enums.NotificationReceiverType;
import me.igrade.events.enums.NotificationType;
import me.igrade.schoolclass.model.Class;
import me.igrade.subject.model.Subject;
import me.igrade.subject.repository.SubjectRepository;
import me.igrade.subject.request.CreateSubjectRequest;
import me.igrade.subject.service.SubjectService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;

    private final KafkaTemplate<String, NotificationCreateEvent> kafkaTemplate;


    public Subject createSubject(CreateSubjectRequest createSubjectRequest, Class c){
        final Subject subject = new Subject(createSubjectRequest.subjectName(),createSubjectRequest.teacherId(),c);

        kafkaTemplate.send("notificationCreateTopic", new NotificationCreateEvent(NotificationReceiverType.TEACHER, NotificationType.SUBJECT,"Stworzono nowy przedmiot, który będzie prowadzony przez Ciebie", createSubjectRequest.teacherId()));

        return subjectRepository.save(subject);

    }
    public Optional<Subject> getSubjectById(long subjectId){
        return subjectRepository.getSubjectById(subjectId);
    }

    public List<Subject> getSubjectsByClassId(int classId){
        return subjectRepository.getSubjectsBySchoolClassId(classId);
    }

    public List<Subject> getSubjectsByTeacherId(int teacherId){
        return subjectRepository.getSubjectsByTeacherId(teacherId);
    }

    public void deleteSubject(long subjectId) {

        subjectRepository.deleteById(subjectId);

    }
}
