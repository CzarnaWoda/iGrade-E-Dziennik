package me.igrade.note.service;


import me.igrade.note.model.Note;
import me.igrade.note.requests.CreateNoteRequest;

import java.util.List;
import java.util.Optional;

public interface NoteService {
    void createNote(CreateNoteRequest createNoteRequest);
    Optional<Note> getNoteByNoteId(long noteId);
    Note updateNote(Long noteId, String description, int points);
    int getStudentPoints(int studentId);
    int getStudentNotesAmount(int studentId);
    List<Note> getNotesByStudentId(int studentId);

    List<Note> getNotesByTeacherId(int teacherId);
    void deleteNote(int noteId);
}
