package com.ddu.evangelical.controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import com.ddu.evangelical.model.Pdf;
import com.ddu.evangelical.repository.PdfRepository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/pdfs")
@CrossOrigin(origins = "http://localhost:3000")
public class PdfController {
    @Autowired
    private PdfRepository pdfRepository;

    private final String UPLOAD_DIR = "./uploads/pdfs/";

    @GetMapping
    public List<Pdf> getAll() { return pdfRepository.findAll(); }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "title", required = false) String title) {
        try {
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) dir.mkdirs();

            String orig = file.getOriginalFilename();
            String filename = UUID.randomUUID().toString() + ".pdf";

            Path path = Paths.get(UPLOAD_DIR + filename);
            Files.write(path, file.getBytes());

            Pdf p = new Pdf();
            p.setTitle(title != null ? title : orig);
            p.setFilePath("/uploads/pdfs/" + filename);
            pdfRepository.save(p);

            return ResponseEntity.ok(p);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        pdfRepository.deleteById(id);
    }
}
