package com.ddu.evangelical.model;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "gifts")
@Data
public class Gift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private BigDecimal amount;

    private String method;

    private String transactionId;

    private String purpose;

    private String note;

    private String status = "PENDING";

    // File upload fields
    @Column(name = "screenshot_path")
    private String screenshotPath;

    @Column(name = "original_filename")
    private String originalFilename;

    private LocalDateTime createdAt;
    private LocalDateTime verifiedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
