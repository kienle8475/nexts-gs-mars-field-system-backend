package com.nexts.gs.mars.nexts_gs_mars_field_service.services;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WebSocketService {

  private final SimpMessagingTemplate messagingTemplate;
  private final Map<String, LocalDateTime> activeSessions = new ConcurrentHashMap<>();

  public WebSocketService(SimpMessagingTemplate messagingTemplate) {
    this.messagingTemplate = messagingTemplate;
  }

  public void registerSession(String sessionId) {
    activeSessions.put(sessionId, LocalDateTime.now());
  }

  public void removeSession(String sessionId) {
    activeSessions.remove(sessionId);
  }

  @Scheduled(fixedRate = 5000) // Send ping every 30 seconds
  public void sendPing() {
    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    Map<String, String> pingMessage = Map.of(
        "type", "ping",
        "timestamp", timestamp,
        "message", "Server ping at " + timestamp);

    messagingTemplate.convertAndSend("/topic/ping", pingMessage);
  }

  public void handlePong(String sessionId) {
    activeSessions.put(sessionId, LocalDateTime.now());
  }

  @Scheduled(fixedRate = 60000) // Check for inactive sessions every minute
  public void checkInactiveSessions() {
    LocalDateTime threshold = LocalDateTime.now().minusSeconds(90); // 90 seconds threshold

    activeSessions.entrySet().removeIf(entry -> {
      if (entry.getValue().isBefore(threshold)) {
        // Session is inactive, remove it
        return true;
      }
      return false;
    });
  }
}