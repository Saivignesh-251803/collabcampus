package com.collabcampus.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity @Table(name="courses")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Course {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false) private String name;
    @Column(unique=true) private String code;
    private String description;
    private int credits;
    private int maxSeats;
    private boolean active = true;

    @ManyToOne @JoinColumn(name="department_id")
    private Department department;

    @ManyToOne @JoinColumn(name="faculty_id")
    private User faculty;

    @CreationTimestamp private LocalDateTime createdAt;
}
