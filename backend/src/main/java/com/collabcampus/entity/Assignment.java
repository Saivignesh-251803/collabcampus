package com.collabcampus.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity @Table(name="assignments")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Assignment {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private String title;
    @Column(columnDefinition="TEXT") private String description;
    private int maxMarks;
    private LocalDateTime deadline;
    @ManyToOne @JoinColumn(name="course_id") private Course course;
    @ManyToOne @JoinColumn(name="faculty_id") private User faculty;
    @CreationTimestamp private LocalDateTime createdAt;
}
