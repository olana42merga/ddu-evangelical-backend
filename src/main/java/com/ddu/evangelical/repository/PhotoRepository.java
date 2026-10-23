package com.ddu.evangelical.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ddu.evangelical.model.Photo;
public interface PhotoRepository extends JpaRepository<Photo, Long> {}
