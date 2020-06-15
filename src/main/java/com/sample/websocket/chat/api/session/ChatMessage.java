package com.sample.websocket.chat.api.session;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatMessage {

    private final String message;

    private final String user;
}
