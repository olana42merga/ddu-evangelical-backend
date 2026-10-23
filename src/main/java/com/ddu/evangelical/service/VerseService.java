package com.ddu.evangelical.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ddu.evangelical.model.Verse;
import com.ddu.evangelical.model.User;
import com.ddu.evangelical.repository.VerseRepository;
import com.ddu.evangelical.repository.UserRepository;
import com.ddu.evangelical.service.email.EmailService;
import java.util.List;

@Service
public class VerseService {
    @Autowired
    private VerseRepository verseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    public List<Verse> getAll() { return verseRepository.findAll(); }

    public Verse getById(Long id) { return verseRepository.findById(id).orElse(null); }

    public Verse save(Verse verse) { return verseRepository.save(verse); }

    public void delete(Long id) { verseRepository.deleteById(id); }

    // ADD VERSE + SEND EMAIL
    public Verse addVerseAndNotify(Verse verse) {
        Verse saved = verseRepository.save(verse);
        sendVerseEmail(saved);
        return saved;
    }

    private void sendVerseEmail(Verse verse) {
        List<User> members = userRepository.findAll();

        String subject = "New Bible Verse: " + verse.getReference();

        // Well-formatted email body
        String body =
            "Dear Member,\n\n" +
            "A new Bible verse has been added:\n\n" +
            "????????????????????????????????????\n" +
            "  " + verse.getReference() + "\n" +
            "????????????????????????????????????\n\n" +
            "\"" + verse.getVerseText() + "\"\n\n" +
            "(" + verse.getTranslation() + ")\n\n" +
            "????????????????????????????????????\n\n" +
            "Login to view: http://localhost:3000/login\n\n" +
            "May this verse strengthen your faith today.\n\n" +
            "God bless you,\n" +
            "DDU Evangelical Team";

        int sent = 0;
        for (User member : members) {
            if (member.getEmail() != null && !member.getEmail().isEmpty()) {
                try {
                    emailService.sendEmail(member.getEmail(), subject, body);
                    sent++;
                } catch (Exception e) {
                    System.out.println("Failed: " + member.getEmail());
                }
            }
        }
        System.out.println("Verse email sent to " + sent + " members");
    }
}
