package com.collabcampus.websocket;

import com.collabcampus.entity.Message;
import com.collabcampus.entity.User;
import com.collabcampus.repository.MessageRepository;
import com.collabcampus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import java.util.Map;

@Controller @RequiredArgsConstructor
public class ChatHandler {
    private final MessageRepository messageRepo;
    private final UserRepository userRepo;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload Map<String,Object> payload) {
        Long senderId = Long.valueOf(payload.get("senderId").toString());
        Long receiverId = Long.valueOf(payload.get("receiverId").toString());
        String content = (String) payload.get("content");

        User sender = userRepo.findById(senderId).orElseThrow();
        User receiver = userRepo.findById(receiverId).orElseThrow();

        Message msg = messageRepo.save(Message.builder()
            .sender(sender).receiver(receiver).content(content).chatType("DIRECT").build());

        messagingTemplate.convertAndSendToUser(
            receiver.getEmail(), "/queue/messages", msg);
        messagingTemplate.convertAndSendToUser(
            sender.getEmail(), "/queue/messages", msg);
    }

    @MessageMapping("/chat.history")
    public void getChatHistory(@Payload Map<String,Object> payload) {
        Long user1 = Long.valueOf(payload.get("user1").toString());
        Long user2 = Long.valueOf(payload.get("user2").toString());
        String requesterEmail = (String) payload.get("requesterEmail");
        var history = messageRepo.findConversation(user1, user2);
        messagingTemplate.convertAndSendToUser(requesterEmail, "/queue/chat-history", history);
    }
}
