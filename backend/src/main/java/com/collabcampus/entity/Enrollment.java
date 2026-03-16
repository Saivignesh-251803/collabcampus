package com.collabcampus.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity @Table(name="enrollments", uniqueConstraints=@UniqueConstraint(columnNames={"student_id","course_id"}))
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Enrollment {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne @JoinColumn(name="student_id") private User student;
    @ManyToOne @JoinColumn(name="course_id") private Course course;
    @CreationTimestamp private LocalDateTime enrolledAt;
}
