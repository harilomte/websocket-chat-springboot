package com.sample.websocket.chat.api.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {

    private final String responseType = "LoginResult";

    private final String status;

}
