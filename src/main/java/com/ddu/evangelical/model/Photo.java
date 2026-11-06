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

    // ADD THIS: Store the actual image in the DB
    @Lob
    @Column(name = "file_data", columnDefinition = "bytea")
    private byte[] fileData;
}