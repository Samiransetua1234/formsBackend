package com.samiransetua.forms.service.interfaces;

import com.samiransetua.forms.entity.Note;
import com.samiransetua.forms.entity.User;

import java.util.List;

public interface NoteService {
    Note createNote(Note note);
    List<Note> getAllNotes();
    List<Note> getNotesByUser(User user);
    Note updateNote(Long id, Note updatedNote);
    void deleteNote(Long id);
}
