package com.ddu.evangelical.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import com.ddu.evangelical.model.Post;
import com.ddu.evangelical.repository.PostRepository;
import com.ddu.evangelical.service.email.EmailService;
import java.io.IOException;
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
                // SAVE IMAGE TO DATABASE
                post.setImageData(image.getBytes());
                post.setImagePath("/api/posts/image/" + UUID.randomUUID().toString());
            }

            Post saved = postRepository.save(post);

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

    // NEW ENDPOINT: View the image from the database
    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> viewImage(@PathVariable Long id) {
        Post post = postRepository.findById(id).orElse(null);
        if (post == null || post.getImageData() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .contentType(MediaType.IMAGE_JPEG)
                .body(post.getImageData());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        postRepository.deleteById(id);
    }
}