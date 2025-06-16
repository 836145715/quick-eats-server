package com.zmx.quickserver.config;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/ws/{clientId}")
public class WebSocketServer {

    private final Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("clientId") String clientId) {
        sessionMap.put(clientId, session);
        System.out.println("新连接，会话 ID: " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("收到消息，会话 ID: " + session.getId() + "，消息内容: " + message);
    }

    @OnClose
    public void onClose(Session session, @PathParam("clientId") String clientId) {
        System.out.println("连接关闭，会话 ID: " + session.getId());
        sessionMap.remove(clientId);
    }

    public void sendMessage(String clientId, String message) {
        Session session = sessionMap.get(clientId);
        if (session != null && session.isOpen()) {
            session.getAsyncRemote().sendText(message);
        }
    }

    public void sendAllMessage(String message) {
        sessionMap.forEach((clientId, session) -> {
            if (session != null && session.isOpen()) {
                session.getAsyncRemote().sendText(message);
            }
        });
    }

}
