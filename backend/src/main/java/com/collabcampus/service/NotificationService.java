package com.collabcampus.service;
import com.collabcampus.entity.*;
import com.collabcampus.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service @RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notifRepo;
    private final UserRepository userRepo;

    public void send(Long userId, String title, String message, String type) {
        User u = userRepo.findById(userId).orElseThrow();
        notifRepo.save(Notification.builder().user(u).title(title).message(message).type(type).read(false).build());
    }

    public List<Notification> getForUser(Long userId) { return notifRepo.findByUserIdOrderByCreatedAtDesc(userId); }
    public long getUnreadCount(Long userId) { return notifRepo.countByUserIdAndReadFalse(userId); }

    public void markAllRead(Long userId) {
        List<Notification> unread = notifRepo.findByUserIdAndReadFalse(userId);
        unread.forEach(n -> n.setRead(true));
        notifRepo.saveAll(unread);
    }
}
