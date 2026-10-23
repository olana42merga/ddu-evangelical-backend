package com.ddu.evangelical.dto;
import lombok.Data;

@Data
public class ProgramRequest {
    private String title;
    private String description;
    private String location;
    private String programDate;
    private String programTime;
    private String notifyEmails;
}
