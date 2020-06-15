package com.sample.websocket.chat.api.session;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class ActiveChatSessions {

    private List<ChatSession> chatSessions = new ArrayList<>();

    public void addChatSession(ChatSession chatSession) {
        chatSessions.add(chatSession);
    }

    public void removeChatSession(ChatSession chatSession) {
        chatSessions.remove(chatSession);
    }


    public Optional<ChatSession> getChatSessionById(String chatSessionId) {
        return chatSessions.stream().filter(chatSession -> {
            return chatSession.getId().equals(chatSessionId);
        }).findFirst();
    }
}
