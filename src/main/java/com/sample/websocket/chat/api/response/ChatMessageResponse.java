package com.sample.websocket.chat.api.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatMessageResponse {

    private final String responseType = "chatMessage";

    private final String fromUser;

    private final String message;
}
