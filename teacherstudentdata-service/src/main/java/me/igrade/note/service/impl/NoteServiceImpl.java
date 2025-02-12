package me.igrade.note.service.impl;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import me.igrade.events.NotificationCreateEvent;
import me.igrade.events.enums.NotificationReceiverType;
import me.igrade.events.enums.NotificationType;
import me.igrade.note.model.Note;
import me.igrade.note.repository.NoteRepository;
import me.igrade.note.requests.CreateNoteRequest;
import me.igrade.response.StudentDTO;
import me.igrade.response.TeacherDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import me.igrade.note.service.NoteService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {


    private final NoteRepository noteRepository;

    private final WebClient.Builder webClientBuilder;

    private final KafkaTemplate<String, NotificationCreateEvent> kafkaTemplate;

    @Override
    public void createNote(CreateNoteRequest createNoteRequest){
        StudentDTO responseStudent = webClientBuilder.build().get()
                .uri("http://user-service/api/v1/student/id/" + createNoteRequest.studentId())
                .retrieve()
                .bodyToMono(StudentDTO.class)
                .block();
        TeacherDTO responseTeacher = webClientBuilder.build().get()
                .uri("http://user-service/api/v1/teacher/id/" + createNoteRequest.teacherId())
                .retrieve()
                .bodyToMono(TeacherDTO.class)
                .block();

        final Note note = new Note(createNoteRequest.points(),createNoteRequest.studentId(),createNoteRequest.teacherId(),createNoteRequest.description(),responseStudent.getFirstName() + " " +  responseStudent.getLastName(), responseTeacher.getFirstName() + " " + responseTeacher.getLastName());

        kafkaTemplate.send("notificationCreateTopic", new NotificationCreateEvent(NotificationReceiverType.STUDENT, NotificationType.NOTE, "Nowa uwaga została wpisana na twoje konto studenckie", createNoteRequest.studentId()));


        noteRepository.save(note);
    }

    @Override
    public Optional<Note> getNoteByNoteId(long noteId){
        return noteRepository.getNoteById(noteId);
    }

    @Override
    @Transactional
    public Note updateNote(Long noteId, String description, int points) {
        Note note = noteRepository.findById(noteId).orElseThrow(() -> new EntityNotFoundException("Note not found"));

        note.setDescription(description);
        note.setPoints(points);

        return noteRepository.save(note);
    }
    @Override
    public int getStudentPoints(int studentId){
        Integer i = noteRepository.getTotalPointsByStudentId(studentId);
        return (i == null  ? 0 : i);
    }
    @Override
    public int getStudentNotesAmount(int studentId){
        Integer i = noteRepository.getTotalNotesAmountByStudentId(studentId);
        return (i == null ? 0 : i);
    }
    @Override
    public List<Note> getNotesByStudentId(int studentId) {
        return noteRepository.getNotesByStudentId(studentId);
    }

    @Override
    public List<Note> getNotesByTeacherId(int teacherId){
        return noteRepository.getNotesByTeacherId(teacherId);
    }

    @Override
    public void deleteNote(int noteId) {
        noteRepository.deleteById(noteId);
    }
}
