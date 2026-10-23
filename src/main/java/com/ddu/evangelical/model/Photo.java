package com.ddu.evangelical.model;
import jakarta.persistence.*;
import lombok.Data;
@Entity
@Table(name = "photos")
@Data
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String filePath;
}
