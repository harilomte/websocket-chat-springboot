package com.sample.websocket.chat.api.session;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

@Getter
@Builder
public class UserSession {

    private final String user;

    private final String sessionId;

    private WebSocketSession webSocketSession;

}
