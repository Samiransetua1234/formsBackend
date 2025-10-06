package com.samiransetua.forms.controller;

import com.samiransetua.forms.entity.Note;
import com.samiransetua.forms.entity.User;
import com.samiransetua.forms.service.interfaces.NoteService;
import com.samiransetua.forms.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notes")
public class NoteController {

    private final NoteService noteService;
    private final UserService userService;


    @Autowired
    public NoteController(NoteService noteService, UserService userService){
        this.noteService = noteService;
        this.userService = userService;
    }

    @GetMapping
    public List<Note> getAllNotes(){
        return noteService.getAllNotes();
    }

    @GetMapping("/user/{userId}")
    public List<Note> getNotesByUser(@PathVariable Long userId){
        User user = userService.findById(userId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return noteService.getNotesByUser(user);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Note createNote(@RequestParam Long userId, @RequestBody Note noteRequest){
        User user = userService.findById(userId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        noteRequest.setCreatedBy(user);
        return noteService.createNote(noteRequest);
    }

    @PutMapping("/{noteId}")
    public Note updateNote(@PathVariable Long noteId, @RequestBody Note updatedNote){
        return noteService.updateNote(noteId, updatedNote);
    }

    @DeleteMapping("/{noteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNote(@PathVariable Long noteId){
        noteService.deleteNote(noteId);
    }

}
