package com.luangeng.ssoclient.service;

import com.luangeng.ssoclient.dto.Note;
import java.util.Collection;

public interface NoteService {
    Collection<Note> getAllNotes();
    Note addNote(Note note);
}
