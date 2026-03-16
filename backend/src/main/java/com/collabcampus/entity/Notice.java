package com.collabcampus.entity;
import com.collabcampus.enums.NoticePriority;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity @Table(name="notices")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Notice {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private String title;
    @Column(columnDefinition="TEXT") private String content;
    @Enumerated(EnumType.STRING) private NoticePriority priority = NoticePriority.GENERAL;
    private boolean approved = true;
    private boolean active = true;
    @ManyToOne @JoinColumn(name="posted_by") private User postedBy;
    @ManyToOne @JoinColumn(name="department_id") private Department department;
    @CreationTimestamp private LocalDateTime createdAt;
}
