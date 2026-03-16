package com.collabcampus.service;
import com.collabcampus.entity.*;
import com.collabcampus.enums.Role;
import com.collabcampus.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service @RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public User getByEmail(String email) { return userRepo.findByEmail(email).orElseThrow(); }
    public User getById(Long id) { return userRepo.findById(id).orElseThrow(); }
    public List<User> getAllUsers() { return userRepo.findAll(); }
    public List<User> getByRole(Role role) { return userRepo.findByRole(role); }

    public User createUser(String name, String email, String password, Role role, Long deptId, String designation) {
        if (userRepo.existsByEmail(email)) throw new RuntimeException("Email already exists");
        User u = User.builder().name(name).email(email)
            .password(passwordEncoder.encode(password))
            .role(role).active(true).designation(designation).build();
        if (deptId != null) {
            Department d = new Department(); d.setId(deptId); u.setDepartment(d);
        }
        return userRepo.save(u);
    }

    public User updateUser(Long id, String name, String phone, String designation) {
        User u = getById(id);
        if (name != null) u.setName(name);
        if (phone != null) u.setPhone(phone);
        if (designation != null) u.setDesignation(designation);
        return userRepo.save(u);
    }

    public void toggleActive(Long id) {
        User u = getById(id);
        u.setActive(!u.isActive());
        userRepo.save(u);
    }

    public void resetPassword(Long id, String newPassword) {
        User u = getById(id);
        u.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(u);
    }

    public long countByRole(Role role) { return userRepo.findByRole(role).size(); }
}
