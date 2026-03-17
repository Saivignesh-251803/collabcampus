package com.collabcampus.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity @Table(name="notifications")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Notification {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne @JoinColumn(name="user_id") private User user;
    private String title;
    private String message;
    private String type;
    @Column(name = "is_read")
    private boolean read;
    @CreationTimestamp private LocalDateTime createdAt;
}
