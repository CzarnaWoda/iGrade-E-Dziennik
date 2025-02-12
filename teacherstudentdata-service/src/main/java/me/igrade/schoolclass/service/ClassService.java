package me.igrade.schoolclass.service;


import me.igrade.schoolclass.model.Class;
import me.igrade.schoolclass.request.CreateClassRequest;
import me.igrade.schoolclass.request.UpdateClassRequest;

import java.util.List;
import java.util.Optional;

public interface ClassService {


    List<Class> getClassesByTeacherId(int teacherId);

    boolean classExistByClassName(String className);


    Class crateClass(CreateClassRequest createClassRequest);

    Class updateClassById(long classId, UpdateClassRequest updateClassRequest);

    Optional<Class> getClassById(long classId);

    Optional<Class> getClassByClassCode(String classCode);

    List<Class> getAllClasses();

    String generateClassCode(String className);

    boolean classExistByClassId(long classId);

    void deleteClassById(long classId);
}
