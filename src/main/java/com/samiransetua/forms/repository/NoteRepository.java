package com.samiransetua.forms.repository;

import com.samiransetua.forms.entity.Note;
import com.samiransetua.forms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByCreatedBy(User user);
}
