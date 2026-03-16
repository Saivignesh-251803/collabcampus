package com.collabcampus.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity @Table(name="attendance_sessions")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AttendanceSession {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne @JoinColumn(name="course_id") private Course course;
    @ManyToOne @JoinColumn(name="faculty_id") private User faculty;
    private String qrToken;
    private LocalDateTime expiresAt;
    private boolean active = true;
    @CreationTimestamp private LocalDateTime createdAt;
}
