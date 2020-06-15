package com.sample.websocket.chat.api.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InitiateChatSessionResponse {

    private final String responseType = "ChatSessionCreationResult";

    private final String status;

    private final String chatSessionId;
}
