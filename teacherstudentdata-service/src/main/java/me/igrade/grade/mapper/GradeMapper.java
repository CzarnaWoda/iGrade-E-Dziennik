package me.igrade.grade.mapper;


import me.igrade.grade.dto.GradeDto;
import me.igrade.grade.model.Grade;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class GradeMapper {

    public GradeDto mapGradeToGradeDto(Grade student){
        GradeDto gradeDto = new GradeDto();
        BeanUtils.copyProperties(student, gradeDto);

        return gradeDto;
    }
}
