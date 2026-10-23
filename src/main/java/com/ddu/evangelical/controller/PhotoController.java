package com.ddu.evangelical.controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import com.ddu.evangelical.model.Photo;
import com.ddu.evangelical.repository.PhotoRepository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/photos")
@CrossOrigin(origins = "http://localhost:3000")
public class PhotoController {
    @Autowired
    private PhotoRepository photoRepository;

    private final String UPLOAD_DIR = "./uploads/photos/";

    @GetMapping
    public List<Photo> getAll() { return photoRepository.findAll(); }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "title", required = false) String title) {
        try {
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) dir.mkdirs();

            String ext = "";
            String orig = file.getOriginalFilename();
            if (orig != null && orig.contains(".")) ext = orig.substring(orig.lastIndexOf("."));
            String filename = UUID.randomUUID().toString() + ext;

            Path path = Paths.get(UPLOAD_DIR + filename);
            Files.write(path, file.getBytes());

            Photo p = new Photo();
            p.setTitle(title != null ? title : orig);
            p.setFilePath("/uploads/photos/" + filename);
            photoRepository.save(p);

            return ResponseEntity.ok(p);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        photoRepository.deleteById(id);
    }
}
