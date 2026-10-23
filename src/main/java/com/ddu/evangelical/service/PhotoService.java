package com.ddu.evangelical.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ddu.evangelical.model.Photo;
import com.ddu.evangelical.repository.PhotoRepository;
import java.util.List;
@Service
public class PhotoService {
    @Autowired
    private PhotoRepository photoRepository;
    public List<Photo> getAll() { return photoRepository.findAll(); }
    public Photo save(Photo photo) { return photoRepository.save(photo); }
    public void delete(Long id) { photoRepository.deleteById(id); }
}
