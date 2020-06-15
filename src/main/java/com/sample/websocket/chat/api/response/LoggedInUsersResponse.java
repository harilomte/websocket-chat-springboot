package com.sample.websocket.chat.api.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class LoggedInUsersResponse {

    private final String responseType = "LoggedInUsers";

    private final List<String> users;
}
