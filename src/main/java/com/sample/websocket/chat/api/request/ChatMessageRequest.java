package com.sample.websocket.chat.api.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatMessageRequest {

    private final String chatSessionId;

    private final String message;
}
