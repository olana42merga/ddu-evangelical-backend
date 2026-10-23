package com.ddu.evangelical.controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.ddu.evangelical.model.Program;
import com.ddu.evangelical.service.ProgramService;
import com.ddu.evangelical.dto.ProgramRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/programs")
@CrossOrigin(origins = "http://localhost:3000")
public class ProgramController {
    @Autowired
    private ProgramService programService;

    @GetMapping
    public List<Program> getAll() { return programService.getAll(); }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody ProgramRequest req) {
        try {
            Program p = new Program();
            p.setTitle(req.getTitle());
            p.setDescription(req.getDescription());
            p.setLocation(req.getLocation());
            if (req.getProgramDate() != null && !req.getProgramDate().isEmpty()) {
                p.setProgramDate(LocalDate.parse(req.getProgramDate()));
            }
            p.setProgramTime(req.getProgramTime());
            p.setNotifyEmails(req.getNotifyEmails());
            p.setReminder1DaySent(false);
            p.setReminder12hSent(false);
            p.setReminder3hSent(false);
            p.setReminder1hSent(false);
            return ResponseEntity.ok(programService.save(p));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { programService.delete(id); }
}
