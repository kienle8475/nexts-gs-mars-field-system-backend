package com.nexts.gs.mars.nexts_gs_mars_field_service.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Controller
public class WebSocketController {

  @MessageMapping("/ping")
  @SendTo("/topic/pong")
  public Map<String, String> handlePing(Map<String, String> message, SimpMessageHeaderAccessor headerAccessor) {
    String sessionId = headerAccessor.getSessionId();
    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

    return Map.of(
        "type", "pong",
        "sessionId", sessionId,
        "timestamp", timestamp,
        "message", "Pong received at " + timestamp);
  }
}