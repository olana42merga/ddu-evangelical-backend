package com.ddu.evangelical.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ddu.evangelical.model.Message;
import com.ddu.evangelical.repository.MessageRepository;
import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public List<Message> getAll() { return messageRepository.findAll(); }

    public Message getById(Long id) { return messageRepository.findById(id).orElse(null); }

    public Message save(Message message) { return messageRepository.save(message); }

    public void delete(Long id) { messageRepository.deleteById(id); }
}
