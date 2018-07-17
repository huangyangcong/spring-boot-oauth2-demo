package com.luangeng.repository;

import com.luangeng.domain.Note;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "note", path = "note")
public interface NoteRepository extends PagingAndSortingRepository<Note, Long> {
}
