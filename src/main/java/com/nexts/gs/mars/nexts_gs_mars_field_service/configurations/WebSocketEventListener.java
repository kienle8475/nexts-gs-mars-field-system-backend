package com.nexts.gs.mars.nexts_gs_mars_field_service.configurations;

import com.nexts.gs.mars.nexts_gs_mars_field_service.services.WebSocketService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {

  private final WebSocketService webSocketService;

  public WebSocketEventListener(WebSocketService webSocketService) {
    this.webSocketService = webSocketService;
  }

  @EventListener
  public void handleWebSocketConnectListener(SessionConnectedEvent event) {
    SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
    String sessionId = headers.getSessionId();
    webSocketService.registerSession(sessionId);
  }

  @EventListener
  public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
    SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
    String sessionId = headers.getSessionId();
    webSocketService.removeSession(sessionId);
  }
}