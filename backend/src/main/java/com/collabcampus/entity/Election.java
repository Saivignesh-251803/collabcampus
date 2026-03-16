package com.collabcampus.entity;
import com.collabcampus.enums.ElectionStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity @Table(name="elections")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Election {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private String title;
    private String description;
    @Enumerated(EnumType.STRING) private ElectionStatus status = ElectionStatus.UPCOMING;
    private LocalDateTime nominationStart;
    private LocalDateTime nominationEnd;
    private LocalDateTime votingStart;
    private LocalDateTime votingEnd;
    @ManyToOne @JoinColumn(name="created_by") private User createdBy;
    @CreationTimestamp private LocalDateTime createdAt;
}
