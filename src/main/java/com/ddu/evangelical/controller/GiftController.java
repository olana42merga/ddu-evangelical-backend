package com.ddu.evangelical.controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import com.ddu.evangelical.model.Gift;
import com.ddu.evangelical.service.GiftService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/gifts")
@CrossOrigin(origins = "http://localhost:3000")
public class GiftController {
    @Autowired
    private GiftService giftService;

    private final String UPLOAD_DIR = "./uploads/gift-proofs/";

    @GetMapping
    public List<Gift> getAll() { return giftService.getAll(); }

    @PostMapping
    public ResponseEntity<?> create(
            @RequestParam("userId") Long userId,
            @RequestParam("amount") BigDecimal amount,
            @RequestParam("method") String method,
            @RequestParam(value = "transactionId", required = false) String transactionId,
            @RequestParam(value = "purpose", required = false) String purpose,
            @RequestParam(value = "note", required = false) String note,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        try {
            Gift gift = new Gift();
            gift.setUserId(userId);
            gift.setAmount(amount);
            gift.setMethod(method);
            gift.setTransactionId(transactionId);
            gift.setPurpose(purpose);
            gift.setNote(note);
            gift.setStatus("PENDING");

            // Save uploaded file if present
            if (file != null && !file.isEmpty()) {
                File dir = new File(UPLOAD_DIR);
                if (!dir.exists()) dir.mkdirs();

                String orig = file.getOriginalFilename();
                String ext = "";
                if (orig != null && orig.contains(".")) ext = orig.substring(orig.lastIndexOf("."));
                String filename = UUID.randomUUID().toString() + ext;

                Path path = Paths.get(UPLOAD_DIR + filename);
                Files.write(path, file.getBytes());

                gift.setScreenshotPath("/uploads/gift-proofs/" + filename);
                gift.setOriginalFilename(orig);
            }

            Gift saved = giftService.save(gift);
            return ResponseEntity.ok(saved);

        } catch (IOException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/verify")
    public Gift verify(@PathVariable Long id) {
        Gift gift = giftService.getById(id);
        if (gift != null) {
            gift.setStatus("VERIFIED");
            gift.setVerifiedAt(java.time.LocalDateTime.now());
            return giftService.save(gift);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { giftService.delete(id); }
}
