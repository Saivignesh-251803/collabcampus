package com.collabcampus.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity @Table(name="clubs")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Club {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private String name;
    private String description;
    private String category;
    private boolean active = true;
    @ManyToOne @JoinColumn(name="lead_id") private User lead;
    @CreationTimestamp private LocalDateTime createdAt;
}
