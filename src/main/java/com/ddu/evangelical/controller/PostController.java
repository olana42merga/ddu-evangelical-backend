package com.ddu.evangelical.controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import com.ddu.evangelical.model.Post;
import com.ddu.evangelical.repository.PostRepository;
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
@CrossOrigin(origins = "http://localhost:3000")
public class PostController {
    @Autowired
    private PostRepository postRepository;

    private final String UPLOAD_DIR = "./uploads/photos/";

    @GetMapping
    public List<Post> getAll() { return postRepository.findAll(); }

    @GetMapping("/{id}")
    public Post getById(@PathVariable Long id) {
        return postRepository.findById(id).orElse(null);
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
                if (!dir.exists()) dir.mkdirs();

                String ext = "";
                String orig = image.getOriginalFilename();
                if (orig != null && orig.contains(".")) ext = orig.substring(orig.lastIndexOf("."));
                String filename = UUID.randomUUID().toString() + ext;

                Path path = Paths.get(UPLOAD_DIR + filename);
                Files.write(path, image.getBytes());

                post.setImagePath("/uploads/photos/" + filename);
            }

            Post saved = postRepository.save(post);
            return ResponseEntity.ok(saved);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            Post post = postRepository.findById(id).orElse(null);
            if (post == null) return ResponseEntity.notFound().build();

            post.setTitle(title);
            post.setContent(content);

            if (image != null && !image.isEmpty()) {
                File dir = new File(UPLOAD_DIR);
                if (!dir.exists()) dir.mkdirs();

                String ext = "";
                String orig = image.getOriginalFilename();
                if (orig != null && orig.contains(".")) ext = orig.substring(orig.lastIndexOf("."));
                String filename = UUID.randomUUID().toString() + ext;

                Path path = Paths.get(UPLOAD_DIR + filename);
                Files.write(path, image.getBytes());

                post.setImagePath("/uploads/photos/" + filename);
            }

            return ResponseEntity.ok(postRepository.save(post));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { postRepository.deleteById(id); }
}
