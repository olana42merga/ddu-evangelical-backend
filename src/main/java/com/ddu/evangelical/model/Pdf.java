package com.ddu.evangelical.model;
import jakarta.persistence.*;
import lombok.Data;
@Entity
@Table(name = "pdfs")
@Data
public class Pdf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String filePath;
}
