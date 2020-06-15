package com.sample.websocket.chat.api.session;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class ChatSession {

    private final String id;

    private final List<String> users;

    @Setter
    @Builder.Default
    private List<ChatMessage> messages = new ArrayList<>();

    public void addMessage(ChatMessage chatMessage) {
        messages.add(chatMessage);
    }
}
