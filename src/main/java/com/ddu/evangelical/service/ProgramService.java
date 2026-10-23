package com.ddu.evangelical.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ddu.evangelical.model.Program;
import com.ddu.evangelical.model.User;
import com.ddu.evangelical.repository.ProgramRepository;
import com.ddu.evangelical.repository.UserRepository;
import com.ddu.evangelical.service.email.EmailService;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ProgramService {
    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    public List<Program> getAll() { return programRepository.findAll(); }
    public Program getById(Long id) { return programRepository.findById(id).orElse(null); }
    public void delete(Long id) { programRepository.deleteById(id); }

    // SAVE + SEND EMAIL IMMEDIATELY
    public Program save(Program program) {
        Program saved = programRepository.save(program);
        sendNewProgramNotification(saved);
        return saved;
    }

    private void sendNewProgramNotification(Program program) {
        List<User> members = userRepository.findAll();

        String subject = "New Program: " + program.getTitle();

        String body =
            "Dear Member,\n\n" +
            "A new program has been scheduled:\n\n" +
            "????????????????????????????????????\n" +
            "  " + program.getTitle() + "\n" +
            "????????????????????????????????????\n\n" +
            "DESCRIPTION:\n" + program.getDescription() + "\n\n" +
            "DATE: " + program.getProgramDate() + "\n" +
            "TIME: " + program.getProgramTime() + "\n" +
            "LOCATION: " + program.getLocation() + "\n\n" +
            "????????????????????????????????????\n\n" +
            "Reminders will be sent 1 day, 12 hours, 3 hours,\n" +
            "and 1 hour before the program starts.\n\n" +
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
        System.out.println("Program email sent to " + sent + " members");
    }

    public void checkAndSendReminders() {
        List<Program> programs = programRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (Program program : programs) {
            if (program.getProgramDate() == null || program.getProgramTime() == null) continue;

            LocalTime time;
            try {
                String t = program.getProgramTime();
                if (t.length() == 5) t += ":00";
                time = LocalTime.parse(t);
            } catch (Exception e) { continue; }

            LocalDateTime programDateTime = LocalDateTime.of(program.getProgramDate(), time);
            long minutesUntil = ChronoUnit.MINUTES.between(now, programDateTime);

            if (minutesUntil > 1380 && minutesUntil <= 1500
                && !Boolean.TRUE.equals(program.getReminder1DaySent())) {
                sendReminder(program, "1 day");
                program.setReminder1DaySent(true);
                programRepository.save(program);
            }
            else if (minutesUntil > 660 && minutesUntil <= 780
                && !Boolean.TRUE.equals(program.getReminder12hSent())) {
                sendReminder(program, "12 hours");
                program.setReminder12hSent(true);
                programRepository.save(program);
            }
            else if (minutesUntil > 150 && minutesUntil <= 210
                && !Boolean.TRUE.equals(program.getReminder3hSent())) {
                sendReminder(program, "3 hours");
                program.setReminder3hSent(true);
                programRepository.save(program);
            }
            else if (minutesUntil > 30 && minutesUntil <= 90
                && !Boolean.TRUE.equals(program.getReminder1hSent())) {
                sendReminder(program, "1 hour");
                program.setReminder1hSent(true);
                programRepository.save(program);
            }
        }
    }

    private void sendReminder(Program program, String timeLeft) {
        List<User> members = userRepository.findAll();
        String subject = "Reminder: " + program.getTitle() + " (" + timeLeft + " left)";

        String body =
            "Dear Member,\n\n" +
            "Reminder: A program starts in " + timeLeft + "!\n\n" +
            "????????????????????????????????????\n" +
            "  " + program.getTitle() + "\n" +
            "????????????????????????????????????\n\n" +
            "DESCRIPTION:\n" + program.getDescription() + "\n\n" +
            "DATE: " + program.getProgramDate() + "\n" +
            "TIME: " + program.getProgramTime() + "\n" +
            "LOCATION: " + program.getLocation() + "\n\n" +
            "See you there!\n\n" +
            "DDU Evangelical Team";

        int sent = 0;
        for (User member : members) {
            if (member.getEmail() != null && !member.getEmail().isEmpty()) {
                try {
                    emailService.sendEmail(member.getEmail(), subject, body);
                    sent++;
                } catch (Exception e) {}
            }
        }
        System.out.println(timeLeft + " reminder sent to " + sent + " members");
    }
}
