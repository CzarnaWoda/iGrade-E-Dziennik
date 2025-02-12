package me.igrade.subject.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.igrade.schoolclass.model.Class;
import me.igrade.schoolclass.service.ClassService;
import me.igrade.subject.dto.SubjectDTO;
import me.igrade.subject.mapper.SubjectMapper;
import me.igrade.subject.model.Subject;
import me.igrade.subject.request.CreateSubjectRequest;
import me.igrade.subject.service.SubjectService;
import me.igrade.utils.HttpResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/subject")

@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    private final SubjectMapper subjectMapper;
    private final ClassService classService;


    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<SubjectDTO>> getTeacherSubjects(@PathVariable int teacherId){
        return ResponseEntity.status(OK).body(subjectService.getSubjectsByTeacherId(teacherId).stream().map(subjectMapper::mapSubjectToSubjectDTO).toList());
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<List<SubjectDTO>> getClassSubjects(@PathVariable int classId){
        return ResponseEntity.status(OK).body(subjectService.getSubjectsByClassId(classId).stream().map(subjectMapper::mapSubjectToSubjectDTO).toList());
    }

    @GetMapping("/id/{subjectId}")
    public ResponseEntity<SubjectDTO> getSubjectById(@PathVariable long subjectId){
        final Optional<Subject> optionalSubject = subjectService.getSubjectById(subjectId);

        return optionalSubject.map(subject -> ResponseEntity.status(OK).body(subjectMapper.mapSubjectToSubjectDTO(subject))).orElseGet(() -> ResponseEntity.status(NOT_FOUND).body(null));
    }

    @DeleteMapping("/delete/{subjectId}")
    public ResponseEntity<HttpResponse> deleteSubjectById(@PathVariable long subjectId){
        subjectService.deleteSubject(subjectId);

        return ResponseEntity.status(OK).body(HttpResponse
                .builder()
                .message("Subject has been deleted")
                .httpStatus(OK)
                .statusCode(OK.value()).build());
    }
    @PostMapping("/create")
    public ResponseEntity<HttpResponse> createSubject(@RequestBody @Valid CreateSubjectRequest createSubjectRequest){
        final Optional<Class> c = classService.getClassById(createSubjectRequest.classId());
        if(c.isEmpty()){
            return ResponseEntity.status(NOT_FOUND).body(HttpResponse.builder().httpStatus(NOT_FOUND)
                    .statusCode(NOT_FOUND.value())
                    .message("Class with this id doesn't exist")
                    .build());
        }
        final Subject subject = subjectService.createSubject(createSubjectRequest,c.get());

        return ResponseEntity.status(OK).body(HttpResponse.builder()
                        .httpStatus(OK)
                        .statusCode(OK.value())
                        .message("Subject has been created")
                        .reason("subject created")
                        .data(Map.of("subject",subject))
                .build());
    }
}
