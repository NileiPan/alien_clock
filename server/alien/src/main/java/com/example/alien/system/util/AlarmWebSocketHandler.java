package com.example.alien.system.util;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class AlarmWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessions = Collections.synchronizedMap(new HashMap<>());

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String sessionId = session.getId();
        sessions.put(sessionId, session);
        try {
            session.sendMessage(new TextMessage("sessionID: " + sessionId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        String sessionId = session.getId();
        String payload = message.getPayload();

        System.out.println("Received message from session " + sessionId + ": " + payload);

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        String sessionId = session.getId();
        sessions.remove(sessionId);

        System.out.println("Connection closed for session " + sessionId);
    }

    public void broadcastAlarm(String message) {
        for (WebSocketSession session : sessions.values()) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendAlarm(String message, String sessionId) {
        WebSocketSession session = sessions.get(sessionId);
        System.out.println("Sending alarm to session " + sessionId);
        if (session != null) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
