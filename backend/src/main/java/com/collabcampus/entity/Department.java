package com.collabcampus.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity @Table(name="departments")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Department {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true) private String name;
    @Column(unique=true) private String code;
    private String description;
    private String hodName;
    private boolean active = true;

    @CreationTimestamp private LocalDateTime createdAt;
}
