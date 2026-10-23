package com.ddu.evangelical.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ddu.evangelical.model.Verse;
public interface VerseRepository extends JpaRepository<Verse, Long> {}
