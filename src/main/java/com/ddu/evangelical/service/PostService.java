package com.ddu.evangelical.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ddu.evangelical.model.Post;
import com.ddu.evangelical.repository.PostRepository;
import java.util.List;
@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    public List<Post> getAll() { return postRepository.findAll(); }
    public Post save(Post post) { return postRepository.save(post); }
    public void delete(Long id) { postRepository.deleteById(id); }
}
