package com.collabcampus.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity @Table(name="grade_appeals")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class GradeAppeal {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne @JoinColumn(name="submission_id") private Submission submission;
    @ManyToOne @JoinColumn(name="student_id") private User student;
    @Column(columnDefinition="TEXT") private String reason;
    private String facultyResponse;
    private String status = "PENDING";
    @CreationTimestamp private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;
}
