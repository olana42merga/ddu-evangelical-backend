package com.ddu.evangelical.controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.ddu.evangelical.model.Message;
import com.ddu.evangelical.service.MessageService;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "http://localhost:3000")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @GetMapping
    public List<Message> getAll() { return messageService.getAll(); }

    @PostMapping
    public Message save(@RequestBody Message message) { return messageService.save(message); }

    @PutMapping("/{id}/reply")
    public Message reply(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Message msg = messageService.getById(id);
        if (msg != null) {
            msg.setReply(body.get("reply"));
            msg.setStatus("REPLIED");
            return messageService.save(msg);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { messageService.delete(id); }
}
