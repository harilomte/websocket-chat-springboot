package com.sample.websocket.chat.api.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InitiateChatRequest {

    private final String chatWithUsers;

}
