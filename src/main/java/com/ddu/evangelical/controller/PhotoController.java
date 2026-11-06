package com.ddu.evangelical.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import com.ddu.evangelical.model.Photo;
import com.ddu.evangelical.repository.PhotoRepository;
import com.ddu.evangelical.service.email.EmailService;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/photos")
@CrossOrigin(origins = "*")
public class PhotoController {

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private EmailService emailService;

    @GetMapping
    public List<Photo> getAll() {
        return photoRepository.findAll();
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "title", required = false) String title) {
        try {
            Photo p = new Photo();
            p.setTitle(title != null ? title : file.getOriginalFilename());

            // SAVE PHOTO TO DATABASE
            p.setFileData(file.getBytes());
            p.setFilePath("/api/photos/view/" + UUID.randomUUID().toString());

            photoRepository.save(p);

            try {
                emailService.sendEmail("obayecha@gmail.com", "New Photo: " + p.getTitle(), "New photo uploaded.");
            } catch (Exception e) {
            }

            return ResponseEntity.ok(p);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ENDPOINT TO VIEW PHOTO
    @GetMapping("/view/{id}")
    public ResponseEntity<byte[]> viewImage(@PathVariable Long id) {
        Photo photo = photoRepository.findById(id).orElse(null);
        if (photo == null || photo.getFileData() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .contentType(MediaType.IMAGE_JPEG)
                .body(photo.getFileData());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        photoRepository.deleteById(id);
    }
}