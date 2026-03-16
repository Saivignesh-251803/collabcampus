package com.collabcampus.entity;
import com.collabcampus.enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity @Table(name="attendance_records")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AttendanceRecord {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne @JoinColumn(name="session_id") private AttendanceSession session;
    @ManyToOne @JoinColumn(name="student_id") private User student;
    @ManyToOne @JoinColumn(name="course_id") private Course course;
    @Enumerated(EnumType.STRING) private AttendanceStatus status = AttendanceStatus.PRESENT;
    @CreationTimestamp private LocalDateTime markedAt;
}
