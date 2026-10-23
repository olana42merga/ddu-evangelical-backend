package com.ddu.evangelical.scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.ddu.evangelical.service.ProgramService;

@Component
public class EmailScheduler {
    @Autowired
    private ProgramService programService;

    // Runs every 30 minutes (at :00 and :30)
    @Scheduled(cron = "0 0,30 * * * ?")
    public void checkProgramReminders() {
        System.out.println("Checking program reminders at " + java.time.LocalDateTime.now());
        programService.checkAndSendReminders();
    }
}
