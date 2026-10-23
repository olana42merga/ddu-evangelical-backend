package com.ddu.evangelical.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ddu.evangelical.model.Gift;
import com.ddu.evangelical.repository.GiftRepository;
import java.util.List;

@Service
public class GiftService {
    @Autowired
    private GiftRepository giftRepository;

    public List<Gift> getAll() { return giftRepository.findAll(); }
    public Gift getById(Long id) { return giftRepository.findById(id).orElse(null); }
    public Gift save(Gift gift) { return giftRepository.save(gift); }
    public void delete(Long id) { giftRepository.deleteById(id); }
}
