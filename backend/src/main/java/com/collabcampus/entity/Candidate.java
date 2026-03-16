package com.collabcampus.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity @Table(name="candidates")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Candidate {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne @JoinColumn(name="election_id") private Election election;
    @ManyToOne @JoinColumn(name="user_id") private User user;
    @Column(columnDefinition="TEXT") private String manifesto;
    private boolean approved = true;
    @CreationTimestamp private LocalDateTime nominatedAt;
}
