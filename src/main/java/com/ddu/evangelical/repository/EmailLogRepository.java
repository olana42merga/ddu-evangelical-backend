package com.ddu.evangelical.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ddu.evangelical.model.EmailLog;
public interface EmailLogRepository extends JpaRepository<EmailLog, Long> {}
