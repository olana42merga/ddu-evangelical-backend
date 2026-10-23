package com.ddu.evangelical.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ddu.evangelical.model.Message;
public interface MessageRepository extends JpaRepository<Message, Long> {}
