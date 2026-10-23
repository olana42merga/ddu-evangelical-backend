package com.ddu.evangelical.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ddu.evangelical.model.User;
import com.ddu.evangelical.repository.UserRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAll() { return userRepository.findAll(); }

    public User getById(Long id) { return userRepository.findById(id).orElse(null); }

    public User save(User user) { return userRepository.save(user); }

    public void delete(Long id) { userRepository.deleteById(id); }

    public User updateMember(Long id, Map<String, String> body) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return null;

        // Check email uniqueness if changing
        String newEmail = body.get("email");
        if (newEmail != null && !newEmail.equals(user.getEmail())) {
            Optional<User> existing = userRepository.findByEmail(newEmail);
            if (existing.isPresent()) {
                throw new RuntimeException("Email already exists: " + newEmail);
            }
            user.setEmail(newEmail);
        }

        if (body.containsKey("firstName")) user.setFirstName(body.get("firstName"));
        if (body.containsKey("lastName")) user.setLastName(body.get("lastName"));
        if (body.containsKey("phone")) user.setPhone(body.get("phone"));
        if (body.containsKey("role")) user.setRole(body.get("role"));

        return userRepository.save(user);
    }

    public void deleteMember(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) throw new RuntimeException("User not found");
        if ("admin".equalsIgnoreCase(user.getUsername())) {
            throw new RuntimeException("Cannot delete admin account");
        }
        userRepository.deleteById(id);
    }
}
