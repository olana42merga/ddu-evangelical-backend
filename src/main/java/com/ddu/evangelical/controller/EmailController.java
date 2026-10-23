package com.ddu.evangelical.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ddu.evangelical.service.email.EmailService;
import com.ddu.evangelical.dto.EmailRequest;
@RestController
@RequestMapping("/api/email")
@CrossOrigin(origins = "http://localhost:3000")
public class EmailController {
    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public String send(@RequestBody EmailRequest request) {
        emailService.sendEmail(request.getTo(), request.getSubject(), request.getBody());
        return "Email sent to " + request.getTo();
    }

    @GetMapping("/test")
    public String test() {
        emailService.sendEmail(
            "obayecha@gmail.com",
            "Test from DDU Backend",
            "This is a test email from your DDU Evangelical Backend. If you see this, email is working!"
        );
        return "Test email sent";
    }
}
