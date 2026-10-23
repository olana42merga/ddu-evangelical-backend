package com.ddu.evangelical.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ddu.evangelical.model.Gift;
public interface GiftRepository extends JpaRepository<Gift, Long> {}
