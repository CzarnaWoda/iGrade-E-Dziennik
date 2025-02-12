package me.igrade.note.repository;


import me.igrade.note.model.Note;
import me.igrade.note.requests.UpdateNoteRequest;
import org.hibernate.sql.Update;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.relational.core.sql.In;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note,Long> {

    Optional<Note> getNoteById(long id);

    List<Note> getNotesByStudentId(int studentId);

    List<Note> getNotesByTeacherId(int teacherId);

    @Query("SELECT SUM(n.points) FROM Note n WHERE n.studentId = :studentId")
    Integer getTotalPointsByStudentId(@Param("studentId") int studentId);

    @Query("SELECT COUNT(n) from Note n where n.studentId = :studentId")
    Integer getTotalNotesAmountByStudentId(@Param("studentId") int studentId);

    @Modifying
    @Query("UPDATE Note n SET n.description = :description, n.points = :points WHERE n.id = :noteId")
    void updateNoteDescriptionPointsByNoteId(@Param("noteId") long noteId, @Param("points") int points, @Param("description") String description);


    void deleteById(long noteId);
}
