package com.ddu.evangelical.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ddu.evangelical.model.Pdf;
import com.ddu.evangelical.repository.PdfRepository;
import java.util.List;
@Service
public class PdfService {
    @Autowired
    private PdfRepository pdfRepository;
    public List<Pdf> getAll() { return pdfRepository.findAll(); }
    public Pdf save(Pdf pdf) { return pdfRepository.save(pdf); }
    public void delete(Long id) { pdfRepository.deleteById(id); }
}
