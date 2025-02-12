package me.igrade.note.controller;


import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.igrade.note.dto.NoteDto;
import me.igrade.note.dto.StatDto;
import me.igrade.note.mapper.NoteMapper;
import me.igrade.note.model.Note;
import me.igrade.note.requests.CreateNoteRequest;
import me.igrade.note.requests.UpdateNoteRequest;
import me.igrade.utils.HttpResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import me.igrade.note.service.NoteService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController

@RequestMapping("/api/v1/note")
public class NoteController {

    private final NoteService noteService;
    private final NoteMapper noteMapper;

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<NoteDto>> notesByStudentId(@PathVariable int studentId){
        return ResponseEntity.status(OK).body(noteService.getNotesByStudentId(studentId).stream().map(noteMapper::mapNoteToNoteDTO).toList());
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<NoteDto>> notesByTeacherId(@PathVariable int teacherId){
        return ResponseEntity.status(OK).body(noteService.getNotesByTeacherId(teacherId).stream().map(noteMapper::mapNoteToNoteDTO).toList());
    }

    @PostMapping("/update/{noteId}")
    public ResponseEntity<HttpResponse> updateNoteByNoteId(@PathVariable long noteId, @RequestBody UpdateNoteRequest noteRequest){
        final Note note = noteService.updateNote(noteId,noteRequest.description(), noteRequest.points());

        return ResponseEntity.status(OK).body(HttpResponse.builder()
                .message("Note has been updated")
                .statusCode(OK.value())
                .data(Map.of("note",note))
                .httpStatus(OK).build());
    }
    @GetMapping("/stats/{studentId}")
    public ResponseEntity<StatDto> getStudentStat(@PathVariable int studentId){

        int points = noteService.getStudentPoints(studentId);
        int amount = noteService.getStudentNotesAmount(studentId);

        final StatDto statDto = new StatDto();
        statDto.setAmount(amount);
        statDto.setPoints(points);

        return ResponseEntity.status(OK).body(statDto);
    }

    @PostMapping("create")
    @CircuitBreaker(name = "note",fallbackMethod = "fallbackMethod")
    public ResponseEntity<HttpResponse> createNote(@RequestBody @Valid CreateNoteRequest createNoteRequest){

        noteService.createNote(createNoteRequest);

        return ResponseEntity.status(OK).body(HttpResponse.builder()
                .message("Note created")
                .statusCode(OK.value())
                .httpStatus(OK).build());
    }
    @DeleteMapping("/delete/{noteId}")
    public ResponseEntity<HttpResponse> deleteNote(@PathVariable int noteId){
        final Optional<Note> optionalNote = noteService.getNoteByNoteId(noteId);

        if(optionalNote.isPresent()){
            noteService.deleteNote(noteId);

            return ResponseEntity.status(OK)
                    .body(HttpResponse.builder()
                            .message("Note has been deleted")
                            .statusCode(OK.value())
                            .httpStatus(OK).build());
        }else{
            return ResponseEntity.status(NOT_FOUND)
                    .body(HttpResponse.builder().message("Note with this id doesn't exist!")
                            .httpStatus(NOT_FOUND)
                            .statusCode(NOT_FOUND.value())
                            .build());
        }
    }

    public ResponseEntity<HttpResponse> fallbackMethod(CreateNoteRequest createNoteRequest, RuntimeException runtimeException){
        return ResponseEntity.status(BAD_REQUEST).body(HttpResponse.builder()
                .httpStatus(BAD_REQUEST)
                .statusCode(BAD_REQUEST.value())
                .message("Oops! Something went wrong! Please wait for a while to make another request!")
                .build());
    }
}
