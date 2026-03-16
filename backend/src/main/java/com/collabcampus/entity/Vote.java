package com.collabcampus.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity @Table(name="votes", uniqueConstraints=@UniqueConstraint(columnNames={"voter_id","election_id"}))
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Vote {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne @JoinColumn(name="voter_id") private User voter;
    @ManyToOne @JoinColumn(name="candidate_id") private Candidate candidate;
    @ManyToOne @JoinColumn(name="election_id") private Election election;
    @CreationTimestamp private LocalDateTime votedAt;
}
