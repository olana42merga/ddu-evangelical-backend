package com.ddu.evangelical.model;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "programs")
@Data
public class Program {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 2000)
    private String description;

    private String location;

    @Column(name = "program_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate programDate;

    @Column(name = "program_time", length = 10)
    private String programTime;

    @Column(name = "notify_emails", length = 500)
    private String notifyEmails;

    @Column(name = "reminder_1day_sent")
    private Boolean reminder1DaySent = false;
    @Column(name = "reminder_12h_sent")
    private Boolean reminder12hSent = false;
    @Column(name = "reminder_3h_sent")
    private Boolean reminder3hSent = false;
    @Column(name = "reminder_1h_sent")
    private Boolean reminder1hSent = false;
}
