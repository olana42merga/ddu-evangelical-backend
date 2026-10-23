package com.ddu.evangelical.dto;
import lombok.Data;
@Data
public class LoginResponse {
    private String token;
    private String role;
}
