package com.collabcampus.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity @Table(name="messages")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Message {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne @JoinColumn(name="sender_id") private User sender;
    @ManyToOne @JoinColumn(name="receiver_id") private User receiver;
    @ManyToOne @JoinColumn(name="course_id") private Course course;
    @Column(columnDefinition="TEXT") private String content;
    @Column(name = "is_read")
    private boolean read;
    private String chatType = "DIRECT";
    @CreationTimestamp private LocalDateTime sentAt;
}
