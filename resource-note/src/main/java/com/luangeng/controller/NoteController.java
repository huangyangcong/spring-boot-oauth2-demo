package com.luangeng.controller;

import com.luangeng.domain.Note;
import com.luangeng.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/note")
public class NoteController {

    @Autowired
    NoteRepository noteRepository;

    @RequestMapping
    public List<Note> noteList() {
        return (List<Note>) noteRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public Note add(@RequestBody Note note) {
        return noteRepository.save(note);
    }

}
