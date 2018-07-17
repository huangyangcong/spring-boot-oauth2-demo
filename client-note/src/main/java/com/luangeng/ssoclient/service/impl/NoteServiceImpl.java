package com.luangeng.ssoclient.service.impl;

import com.luangeng.ssoclient.dto.Note;
import com.luangeng.ssoclient.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.hateoas.Resources;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;


@Service
public class NoteServiceImpl implements NoteService {

    @Autowired
    OAuth2RestTemplate restTemplate;

    @Value("${resource-server}/note")
    private String notesURL;

    @Override
    public Collection<Note> getAllNotes() {
        Resources<Note> notes = restTemplate.getForObject(notesURL, Resources.class);
        return notes.getContent();
    }

    @Override
    public Note addNote(Note note) {
        ResponseEntity<Note> response = restTemplate.postForEntity(notesURL, note, Note.class);
        return response.getBody();
    }
}
