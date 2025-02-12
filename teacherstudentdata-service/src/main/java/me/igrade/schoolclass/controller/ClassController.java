package me.igrade.schoolclass.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.igrade.schoolclass.model.Class;
import me.igrade.schoolclass.request.CreateClassRequest;
import me.igrade.schoolclass.request.UpdateClassRequest;
import me.igrade.schoolclass.service.ClassService;
import me.igrade.utils.HttpResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/class")
public class ClassController {

    private final ClassService classService;



    @PostMapping("/create")
    public ResponseEntity<HttpResponse> createClass(@Valid @RequestBody CreateClassRequest createClassRequest){
        if(classService.classExistByClassName(createClassRequest.className())){
            return ResponseEntity.status(FOUND)
                    .body(HttpResponse.builder()
                            .httpStatus(FOUND)
                            .statusCode(FOUND.value())
                            .message("Class with this name already exist").build());
        }

        final Class c = classService.crateClass(createClassRequest);

        return ResponseEntity.status(CREATED)
                .body(HttpResponse.builder()
                        .httpStatus(CREATED)
                        .statusCode(CREATED.value())
                        .message("Class has been created")
                        .data(Map.of("class",c)).build());
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<Class>> getClassesByTeacherId(@PathVariable int teacherId){

        return ResponseEntity.status(OK).body(classService.getClassesByTeacherId(teacherId));
    }

    @GetMapping("/id/{classId}")
    public ResponseEntity<Class> getClassById(@PathVariable long classId){
        final Optional<Class> optionalClass = classService.getClassById(classId);

        return optionalClass.map(aClass -> ResponseEntity.status(OK).body(aClass)).orElseGet(() -> ResponseEntity.status(NOT_FOUND).body(null));
    }

    @GetMapping("/code/{classCode}")
    public ResponseEntity<Class> getClassByClassCode(@PathVariable String classCode){

        final Optional<Class> classOptional = classService.getClassByClassCode(classCode);

        return classOptional.map(aClass ->
                ResponseEntity.status(OK).body(classOptional.get()))
                .orElseGet(() -> ResponseEntity.status(NOT_FOUND).body(null));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpResponse> deleteClassById(@PathVariable long id){
        classService.deleteClassById(id);

        return ResponseEntity.status(OK)
                .body(HttpResponse.builder()
                        .httpStatus(OK)
                        .statusCode(OK.value())
                        .message("Class has been deleted").build());
    }

    @PostMapping("/update/{classId}")
    public ResponseEntity<HttpResponse> updateClass(@PathVariable long classId, @RequestBody UpdateClassRequest updateClassRequest){
        final Class c = classService.updateClassById(classId,updateClassRequest);

        if(c != null){
            return ResponseEntity.status(OK)
                    .body(HttpResponse.builder()
                            .message("Class has been updated")
                            .data(Map.of("class", c)).httpStatus(OK)
                            .statusCode(OK.value()).build());
        }else{
            return ResponseEntity.status(NOT_FOUND)
                    .body(HttpResponse.builder()
                            .httpStatus(NOT_FOUND)
                            .statusCode(NOT_FOUND.value())
                            .message("Class with this id doesn't exist").build());
        }

    }

    @GetMapping("/all")
    public ResponseEntity<List<Class>> getClasses(){

        return ResponseEntity.status(OK).body(classService.getAllClasses());
    }


}
