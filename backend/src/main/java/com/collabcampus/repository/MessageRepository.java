package com.collabcampus.repository;
import com.collabcampus.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
public interface MessageRepository extends JpaRepository<Message,Long> {
    @Query("SELECT m FROM Message m WHERE (m.sender.id=?1 AND m.receiver.id=?2) OR (m.sender.id=?2 AND m.receiver.id=?1) ORDER BY m.sentAt ASC")
    List<Message> findConversation(Long user1, Long user2);
    List<Message> findByCourseIdOrderBySentAtAsc(Long courseId);
    long countByReceiverIdAndReadFalse(Long receiverId);
}
