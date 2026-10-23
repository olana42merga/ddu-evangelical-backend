package com.ddu.evangelical.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ddu.evangelical.model.Program;
public interface ProgramRepository extends JpaRepository<Program, Long> {}
