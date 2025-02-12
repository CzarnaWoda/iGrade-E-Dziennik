package me.igrade.note.mapper;

import me.igrade.note.dto.NoteDto;
import me.igrade.note.model.Note;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class NoteMapper {

    public NoteDto mapNoteToNoteDTO(Note note){

        final NoteDto noteDto = new NoteDto();

        BeanUtils.copyProperties(note,noteDto);

        return noteDto;
    }

}
