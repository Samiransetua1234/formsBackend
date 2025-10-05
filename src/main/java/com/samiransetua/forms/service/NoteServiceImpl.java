package com.samiransetua.forms.service;

import com.samiransetua.forms.entity.Note;
import com.samiransetua.forms.entity.User;
import com.samiransetua.forms.repository.NoteRepository;
import com.samiransetua.forms.service.interfaces.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Override
    public Note createNote(Note note) {
        return noteRepository.save(note);
    }

    @Override
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    @Override
    public List<Note> getNotesByUser(User user) {
        return noteRepository.findByCreatedBy(user);
    }

    @Override
    public Note updateNote(Long id, Note updatedNote) {
        return noteRepository.findById(id).map(note -> {
            note.setTitle(updatedNote.getTitle());
            note.setContent(updatedNote.getContent());
            return noteRepository.save(note);
        }).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found"));
    }

    @Override
    public void deleteNote(Long id) {
        if (!noteRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Note not found");
        }
        noteRepository.deleteById(id);
    }
}
