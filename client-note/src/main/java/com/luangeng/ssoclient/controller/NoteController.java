package com.luangeng.ssoclient.controller;

import com.luangeng.ssoclient.dto.Note;
import com.luangeng.ssoclient.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class NoteController {

    @Autowired
    NoteService noteService;

    @RequestMapping("/")
    public String home() {
        return "redirect:/notes";
    }

    @RequestMapping("/notes")
    public String notes(Model model) {
        model.addAttribute("notes", noteService.getAllNotes());
        return "index";
    }

    @RequestMapping("/add")
    public String add(Model model) {
        model.addAttribute("note", new Note());
        return "add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(Note note, Model model) {
        Note savedNote = noteService.addNote(note);

        if(savedNote != null){
            return "redirect:/notes";
        }else{
            model.addAttribute("note", note);
            return "add?error";
        }

    }
}
