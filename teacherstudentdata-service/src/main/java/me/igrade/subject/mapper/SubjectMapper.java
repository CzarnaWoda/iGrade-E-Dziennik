package me.igrade.subject.mapper;


import lombok.RequiredArgsConstructor;
import me.igrade.schoolclass.model.Class;
import me.igrade.schoolclass.service.ClassService;
import me.igrade.subject.dto.SubjectDTO;
import me.igrade.subject.model.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SubjectMapper {

    private final ClassService classService;

    public SubjectDTO mapSubjectToSubjectDTO(Subject subject){

        final SubjectDTO subjectDTO = new SubjectDTO();

        BeanUtils.copyProperties(subject, subjectDTO);

        final Class c = subject.getSchoolClass();
        subjectDTO.setClassName(c.getClassName());

        subjectDTO.setClassId(c.getId());

        return subjectDTO;
    }
}
