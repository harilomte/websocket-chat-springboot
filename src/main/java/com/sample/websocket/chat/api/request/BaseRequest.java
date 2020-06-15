package com.sample.websocket.chat.api.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BaseRequest {

    private final RequestType requestType;

    public enum RequestType {
        LOGIN, INITIATE_CHAT, CHAT_MSG
    }

}
