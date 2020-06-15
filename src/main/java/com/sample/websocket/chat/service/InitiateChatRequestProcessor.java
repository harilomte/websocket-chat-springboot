package com.sample.websocket.chat.service;

import com.google.gson.Gson;
import com.sample.websocket.chat.api.request.InitiateChatRequest;
import com.sample.websocket.chat.api.response.InitiateChatSessionResponse;
import com.sample.websocket.chat.api.session.ActiveChatSessions;
import com.sample.websocket.chat.api.session.ActiveUserSessions;
import com.sample.websocket.chat.api.session.ChatSession;
import com.sample.websocket.chat.api.session.UserSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Arrays;
import java.util.UUID;

@AllArgsConstructor
@Slf4j
public class InitiateChatRequestProcessor {
    private final Gson gson = new Gson();

    private ActiveUserSessions activeUserSessions;
    private ActiveChatSessions activeChatSessions;

    public void process(WebSocketSession session, String payload) throws Exception {

        log.info(String.format("initiating chat for session: %s\n", session.getId()));
        InitiateChatRequest initiateChatRequest = gson.fromJson(payload, InitiateChatRequest.class);

        //TODO: validate request

        String userToChatWith = initiateChatRequest.getChatWithUsers();
        UserSession currentUserSession = activeUserSessions.getUserSessionById(session.getId()).get();
        String currentUser = currentUserSession.getUser();

        ChatSession chatSession = ChatSession.builder()
                .id(UUID.randomUUID().toString())
                .users(Arrays.asList(currentUser, userToChatWith))
                .build();

        activeChatSessions.addChatSession(chatSession);

        System.out.printf("chat initiated for user: %s, session: %s, usersToChatWith: %s\n", currentUser,
                session.getId(), userToChatWith);

        InitiateChatSessionResponse initiateChatSessionResponse = InitiateChatSessionResponse.builder()
                .status("200").chatSessionId(chatSession.getId()).build();

        TextMessage textMessage = new TextMessage(gson.toJson(initiateChatSessionResponse));
        session.sendMessage(textMessage);

        activeUserSessions.getUserSessionByName(userToChatWith).get().getWebSocketSession().sendMessage(textMessage);
    }

}
