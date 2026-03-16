package com.collabcampus.config;

import com.collabcampus.entity.*;
import com.collabcampus.enums.*;
import com.collabcampus.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component @RequiredArgsConstructor @Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepo;
    private final DepartmentRepository deptRepo;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email}") private String adminEmail;
    @Value("${app.admin.password}") private String adminPassword;
    @Value("${app.university.name}") private String universityName;

    @Override
    public void run(String... args) {
        seedAdmin();
        seedDepartments();
    }

    private void seedAdmin() {
        if (!userRepo.existsByEmail(adminEmail)) {
            User admin = User.builder()
                .name("Admin")
                .email(adminEmail)
                .password(passwordEncoder.encode(adminPassword))
                .role(Role.ADMIN)
                .active(true)
                .designation("System Administrator")
                .build();
            userRepo.save(admin);
            log.info("✅ Admin account created: {}", adminEmail);
        } else {
            log.info("ℹ️  Admin already exists: {}", adminEmail);
        }
    }

    private void seedDepartments() {
        if (deptRepo.count() == 0) {
            String[][] depts = {
                {"Computer Science & Engineering","CSE"},
                {"Electronics & Communication","ECE"},
                {"Mechanical Engineering","MECH"},
                {"Civil Engineering","CIVIL"},
                {"Business Administration","MBA"}
            };
            for (String[] d : depts) {
                deptRepo.save(Department.builder()
                    .name(d[0]).code(d[1]).active(true)
                    .description(d[0] + " Department - " + universityName)
                    .build());
            }
            log.info("✅ Sample departments seeded");
        }
    }
}
