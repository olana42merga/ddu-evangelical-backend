package com.ddu.evangelical.model;
import jakarta.persistence.*;
import lombok.Data;
@Entity
@Table(name = "verses")
@Data
public class Verse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String reference;
    private String verseText;
    private String translation;
}
