package com.sample.websocket.chat.service;

import com.google.gson.Gson;
import com.sample.websocket.chat.api.request.LoginRequest;
import com.sample.websocket.chat.api.response.LoggedInUsersResponse;
import com.sample.websocket.chat.api.response.LoginResponse;
import com.sample.websocket.chat.api.session.ActiveUserSessions;
import com.sample.websocket.chat.api.session.UserSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@AllArgsConstructor
@Slf4j
public class LoginRequestProcessor {

    private final Gson gson = new Gson();

    private ActiveUserSessions activeUserSessions;

    public void process(WebSocketSession session, String payload) throws Exception {

        doLogin(session, payload);
        sendActiveUserListToAllUsers(session, payload);
    }

    private void doLogin(WebSocketSession session, String payload) throws Exception {

        LoginRequest loginRequest = gson.fromJson(payload, LoginRequest.class);

        log.info(String.format("user %s logged in, session: %s", loginRequest.getUser(), session.getId()));

        // supporting only 1 session per user.
        activeUserSessions.addUserSession(UserSession.builder().user(loginRequest.getUser()).sessionId(session.getId())
                .webSocketSession(session).build());

        LoginResponse loginResponse = LoginResponse.builder().status("200").build();
        session.sendMessage(new TextMessage(gson.toJson(loginResponse)));
    }

    private void sendActiveUserListToAllUsers(WebSocketSession session, String payload) {

        // For each user, send list of active user names
        LoggedInUsersResponse loggedInUsersResponse = LoggedInUsersResponse.builder().users(activeUserSessions.getAllUserNames()).build();
        activeUserSessions.getUserSessions().stream().forEach(userSession ->  {
            try {
                String username = userSession.getUser();
                userSession.getWebSocketSession().sendMessage(new TextMessage(gson.toJson(loggedInUsersResponse)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}


