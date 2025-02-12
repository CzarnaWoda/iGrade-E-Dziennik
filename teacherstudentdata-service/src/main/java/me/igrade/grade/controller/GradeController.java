package me.igrade.grade.controller;


import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.igrade.grade.dto.GradeDto;
import me.igrade.grade.requests.CreateGradeRequest;
import me.igrade.grade.service.GradeService;
import me.igrade.utils.HttpResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/grade")
public class GradeController {

    private final GradeService gradeService;
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<GradeDto>> gradesByStudentId(@PathVariable int studentId){

        final List<GradeDto> grades = gradeService.getGradesByStudentId(studentId);

        if(grades.isEmpty()){
            return ResponseEntity.status(OK).body(Collections.emptyList());
        }
        return ResponseEntity.status(OK).body(grades);
    }
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<GradeDto>> gradesByTeacherId(@PathVariable int teacherId){

        final List<GradeDto> grades = gradeService.getGradesByTeacherId(teacherId);

        if(grades.isEmpty()){
            return ResponseEntity.status(OK).body(Collections.emptyList());
        }
        return ResponseEntity.status(OK).body(grades);
    }

    @PostMapping("/create")
    @CircuitBreaker(name = "grade", fallbackMethod = "fallbackMethod")
    //@TimeLimiter(name = "grade")
    public ResponseEntity<HttpResponse> createGrade(@RequestBody @Valid CreateGradeRequest createGradeRequest){
        if(gradeService.createGrade(createGradeRequest.grade(), createGradeRequest.teacherId(), createGradeRequest.studentId(), createGradeRequest.subjectId())) {
            return ResponseEntity.status(OK).body(HttpResponse.builder()
                    .httpStatus(OK)
                    .statusCode(OK.value())
                    .message("Grade created").build());
        }else{
            return ResponseEntity.status(BAD_REQUEST).body(HttpResponse.builder()
                    .httpStatus(BAD_REQUEST)
                    .statusCode(BAD_REQUEST.value())
                    .message("Grade can not be created").build());
        }
    }

    @DeleteMapping("/delete/{gradeId}")
    public ResponseEntity<HttpResponse> deleteGrade(@PathVariable long gradeId){
        if(gradeService.existById(gradeId)) {
            if (gradeService.deleteGrade(gradeId)) {
                return ResponseEntity.status(OK).body(HttpResponse.builder()
                        .httpStatus(OK)
                        .statusCode(OK.value())
                        .message("Grade has been deleted")
                        .build());
            }else{
                return ResponseEntity.status(BAD_REQUEST).body(HttpResponse
                        .builder()
                        .httpStatus(BAD_REQUEST)
                        .statusCode(BAD_REQUEST.value())
                        .message("Something went wrong!")
                        .build());
            }
        }else{
            return ResponseEntity.status(NOT_FOUND).body(HttpResponse
                    .builder()
                    .httpStatus(NOT_FOUND)
                    .statusCode(NOT_FOUND.value())
                    .message("Grade with this id doesn't exist!")
                    .build());
        }

    }

    public ResponseEntity<HttpResponse> fallbackMethod(CreateGradeRequest createGradeRequest, RuntimeException runtimeException){
        return ResponseEntity.status(BAD_REQUEST).body(HttpResponse.builder()
                .httpStatus(BAD_REQUEST)
                .statusCode(BAD_REQUEST.value())
                .message("Oops! Something went wrong! Please wait for a while to make another request!")
                .build());
    }
}
