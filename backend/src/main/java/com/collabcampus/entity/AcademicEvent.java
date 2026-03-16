package com.collabcampus.entity;
import com.collabcampus.enums.EventType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity @Table(name="academic_events")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AcademicEvent {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private String title;
    private String description;
    @Enumerated(EnumType.STRING) private EventType type = EventType.GENERAL;
    private LocalDate eventDate;
    private LocalDate endDate;
    private boolean allDay = true;
    @ManyToOne @JoinColumn(name="department_id") private Department department;
    @ManyToOne @JoinColumn(name="created_by") private User createdBy;
    @CreationTimestamp private LocalDateTime createdAt;
}
