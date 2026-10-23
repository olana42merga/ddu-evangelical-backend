package com.ddu.evangelical.controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.ddu.evangelical.model.Verse;
import com.ddu.evangelical.service.VerseService;
import java.util.List;

@RestController
@RequestMapping("/api/verses")
@CrossOrigin(origins = "http://localhost:3000")
public class VerseController {
    @Autowired
    private VerseService verseService;

    @GetMapping
    public List<Verse> getAll() { return verseService.getAll(); }

    @GetMapping("/{id}")
    public Verse getById(@PathVariable Long id) { return verseService.getById(id); }

    @PostMapping
    public Verse save(@RequestBody Verse verse) {
        return verseService.addVerseAndNotify(verse);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { verseService.delete(id); }
}
