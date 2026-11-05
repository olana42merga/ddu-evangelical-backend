package com.ddu.evangelical.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import com.ddu.evangelical.model.Post;
import com.ddu.evangelical.repository.PostRepository;
import com.ddu.evangelical.service.email.EmailService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "*")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private EmailService emailService;

    private final String UPLOAD_DIR = "./uploads/photos/";

    @GetMapping
    public List<Post> getAll() {
        return postRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> create(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            Post post = new Post();
            post.setTitle(title);
            post.setContent(content);

            if (image != null && !image.isEmpty()) {
                File dir = new File(UPLOAD_DIR);
                if (!dir.exists())
                    dir.mkdirs();

                String orig = image.getOriginalFilename();
                String ext = "";
                if (orig != null && orig.contains("."))
                    ext = orig.substring(orig.lastIndexOf("."));
                String filename = UUID.randomUUID().toString() + ext;

                Path path = Paths.get(UPLOAD_DIR + filename);
                Files.write(path, image.getBytes());

                post.setImagePath("/uploads/photos/" + filename);
            }

            Post saved = postRepository.save(post);

            // Send Email
            try {
                emailService.sendEmail(
                        "obayecha@gmail.com",
                        "New Post Created: " + title,
                        "A new post was created:\n\nTitle: " + title + "\n\nContent:\n" + content);
                System.out.println("✅ Email notification sent.");
            } catch (Exception e) {
                System.out.println("❌ Email failed: " + e.getMessage());
            }

            return ResponseEntity.ok(saved);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        postRepository.deleteById(id);
    }
}