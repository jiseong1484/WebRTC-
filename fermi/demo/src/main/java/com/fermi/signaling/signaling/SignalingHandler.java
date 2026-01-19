package com.fermi.signaling.signaling; // <-- 너의 패키지명으로 변경

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SignalingHandler extends TextWebSocketHandler {

    private final ObjectMapper om = new ObjectMapper();

    // roomId -> (그 방에 들어온 웹소켓 세션들)
    private final Map<String, Set<WebSocketSession>> rooms = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        // 연결이 끊기면 어떤 방에 있든 제거
        rooms.values().forEach(set -> set.remove(session));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JsonNode root = om.readTree(message.getPayload());

        String type = root.path("type").asText("");
        String roomId = root.path("roomId").asText("");

        if (roomId.isBlank() || type.isBlank()) return;

        switch (type) {
            case "join" -> {
                rooms.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(session);
            }
            case "leave" -> {
                Set<WebSocketSession> set = rooms.get(roomId);
                if (set != null) set.remove(session);
            }
            case "offer", "answer", "candidate", "ended" -> {
                relayToOthers(roomId, session, message);
            }
            default -> {
                // ignore
            }
        }
    }

    private void relayToOthers(String roomId, WebSocketSession sender, TextMessage msg) throws Exception {
        Set<WebSocketSession> set = rooms.get(roomId);
        if (set == null) return;

        for (WebSocketSession s : set) {
            if (!s.isOpen()) continue;
            if (s.getId().equals(sender.getId())) continue;
            s.sendMessage(msg); // 메시지 그대로 브로드캐스트(보낸 사람 제외)
        }
    }
}