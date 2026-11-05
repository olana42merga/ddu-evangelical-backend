package com.ddu.evangelical.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.ddu.evangelical.model.Verse;
import com.ddu.evangelical.service.VerseService;
import com.ddu.evangelical.service.email.EmailService; // ADDED
import java.util.List;

@RestController
@RequestMapping("/api/verses")
@CrossOrigin(origins = "*")
public class VerseController {

    @Autowired
    private VerseService verseService;

    @Autowired
    private EmailService emailService; // ADDED

    @GetMapping
    public List<Verse> getAll() {
        return verseService.getAll();
    }

    @GetMapping("/{id}")
    public Verse getById(@PathVariable Long id) {
        return verseService.getById(id);
    }

    @PostMapping
    public Verse save(@RequestBody Verse verse) {
        Verse savedVerse = verseService.addVerseAndNotify(verse);

        // SEND EMAIL
        try {
            emailService.sendEmail(
                    "obayecha@gmail.com",
                    "New Daily Verse Added: " + savedVerse.getReference(),
                    "A new verse has been added:\n\n\"" + savedVerse.getVerseText() + "\"\n\n- "
                            + savedVerse.getTranslation());
        } catch (Exception e) {
            System.out.println("Email failed: " + e.getMessage());
        }

        return savedVerse;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        verseService.delete(id);
    }
}