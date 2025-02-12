package me.igrade.schoolclass.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.igrade.events.NotificationCreateEvent;
import me.igrade.events.enums.NotificationReceiverType;
import me.igrade.events.enums.NotificationType;
import me.igrade.schoolclass.model.Class;
import me.igrade.schoolclass.repository.ClassRepository;
import me.igrade.schoolclass.request.CreateClassRequest;
import me.igrade.schoolclass.request.UpdateClassRequest;
import me.igrade.schoolclass.service.ClassService;
import me.igrade.utils.RandomUtil;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClassServiceImpl implements ClassService {


    private final ClassRepository classRepository;
    private final KafkaTemplate<String, NotificationCreateEvent> kafkaTemplate;

    @Override
    public List<Class> getClassesByTeacherId(int teacherId){
        return classRepository.getClassesByTeacherId(teacherId);
    }

    @Override
    public boolean classExistByClassName(String className){
        return classRepository.existsByClassName(className);
    }

    @Override
    public Class crateClass(CreateClassRequest createClassRequest){
        String code = generateClassCode(createClassRequest.className());
        while (classRepository.existsByClassCode(code)){
            code = generateClassCode(createClassRequest.className());
        }

        kafkaTemplate.send("notificationCreateTopic", new NotificationCreateEvent(NotificationReceiverType.STUDENT, NotificationType.CLASS,"Stworzono nową klase!", createClassRequest.teacherId()));

        return classRepository.save(new Class(createClassRequest.className(),createClassRequest.teacherId(), generateClassCode(createClassRequest.className())));
    }
    @Override
    public Class updateClassById(long classId, UpdateClassRequest updateClassRequest){
        final Optional<Class> optionalClass = getClassById(classId);
        if(optionalClass.isPresent()){
            final Class c = optionalClass.get();

            c.setClassCode(updateClassRequest.classCode());
            c.setClassName(updateClassRequest.className());
            classRepository.save(c);

            return c;
        }
        return null;
    }
    @Override
    public Optional<Class> getClassById(long classId){
        return classRepository.getClassById(classId);
    }
    @Override
    public Optional<Class> getClassByClassCode(String classCode){
        return classRepository.getClassByClassCode(classCode);
    }
    @Override
    public List<Class> getAllClasses(){
        return classRepository.findAll();
    }
    @Override
    public String generateClassCode(String className){
        StringBuilder code = new StringBuilder();

        for(int i = 0 ; i < 3 ; i ++){
            code.append(RandomUtil.getRandomChar());
        }

        return code + "-" + className;
    }
    @Override
    public boolean classExistByClassId(long classId) {
        return classRepository.existsById(classId);
    }
    @Override
    public void deleteClassById(long classId) {

        classRepository.deleteById(classId);
    }
}
