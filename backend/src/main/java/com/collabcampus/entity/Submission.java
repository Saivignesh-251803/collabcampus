package com.collabcampus.entity;
import com.collabcampus.enums.SubmissionStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity @Table(name="submissions")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Submission {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne @JoinColumn(name="assignment_id") private Assignment assignment;
    @ManyToOne @JoinColumn(name="student_id") private User student;
    @Column(columnDefinition="TEXT") private String content;
    private Integer marksObtained;
    private String feedback;
    @Enumerated(EnumType.STRING) private SubmissionStatus status = SubmissionStatus.SUBMITTED;
    @CreationTimestamp private LocalDateTime submittedAt;
    private LocalDateTime gradedAt;
}
