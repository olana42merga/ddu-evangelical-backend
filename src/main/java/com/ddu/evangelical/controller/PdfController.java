package com.ddu.evangelical.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import com.ddu.evangelical.model.Pdf;
import com.ddu.evangelical.repository.PdfRepository;
import com.ddu.evangelical.service.email.EmailService;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/pdfs")
@CrossOrigin(origins = "*")
public class PdfController {

    @Autowired
    private PdfRepository pdfRepository;

    @Autowired
    private EmailService emailService;

    @GetMapping
    public List<Pdf> getAll() {
        return pdfRepository.findAll();
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "title", required = false) String title) {
        try {
            Pdf p = new Pdf();
            p.setTitle(title != null ? title : file.getOriginalFilename());
            p.setFileData(file.getBytes());
            p.setFilePath("/api/pdfs/download/" + UUID.randomUUID().toString());

            pdfRepository.save(p);

            try {
                emailService.sendEmail("obayecha@gmail.com", "New PDF: " + p.getTitle(), "New PDF uploaded.");
            } catch (Exception e) {
            }

            return ResponseEntity.ok(p);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id) {
        Pdf pdf = pdfRepository.findById(id).orElse(null);
        if (pdf == null || pdf.getFileData() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + pdf.getTitle() + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf.getFileData());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        pdfRepository.deleteById(id);
    }
}