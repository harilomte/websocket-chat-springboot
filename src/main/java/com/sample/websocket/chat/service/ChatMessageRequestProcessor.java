package com.sample.websocket.chat.service;

import com.google.gson.Gson;
import com.sample.websocket.chat.api.request.ChatMessageRequest;
import com.sample.websocket.chat.api.response.ChatMessageResponse;
import com.sample.websocket.chat.api.session.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
public class ChatMessageRequestProcessor {

    private final Gson gson = new Gson();

    private ActiveUserSessions activeUserSessions;
    private ActiveChatSessions activeChatSessions;

    public void process(WebSocketSession session, String payload) throws Exception {

        log.info(String.format("chat message received for session: %s\n", session.getId()));
        ChatMessageRequest chatMessageRequest = gson.fromJson(payload, ChatMessageRequest.class);

        Optional<ChatSession> chatSession = activeChatSessions.getChatSessionById(chatMessageRequest.getChatSessionId());

        if (chatSession.isPresent()) {
            log.info(String.format("chat session found: %s\n", chatSession.get().getId()));

            UserSession currentUserSession = activeUserSessions.getUserSessionById(session.getId()).get();
            String currentUser = currentUserSession.getUser();

            chatSession.get().addMessage(ChatMessage.builder().message(chatMessageRequest.getMessage()).user(currentUser).build());

            //notify other users
            notifyUsersInChatSession(chatMessageRequest, chatSession, currentUser);
        }
    }

    private void notifyUsersInChatSession(ChatMessageRequest chatMessageRequest, Optional<ChatSession> chatSession, String currentUser) {
        // get all users in chat session except for current user.
        List<String> usersToBeNotified = chatSession.get().getUsers()
                .stream().filter(user -> !currentUser.equalsIgnoreCase(user)).collect(Collectors.toList());
        log.info(String.format("users to be notified: %s\n", usersToBeNotified));

        usersToBeNotified.stream().map(activeUserSessions::getUserSessionByName).forEach(userSession -> {
            try {
                if (userSession.isPresent()) {
                    log.info(String.format("user session notified: %s, message: %s\n", userSession.get().getSessionId()
                            , chatMessageRequest.getMessage()));

                    ChatMessageResponse chatMessageResponse =
                            ChatMessageResponse.builder().fromUser(currentUser)
                                    .message(chatMessageRequest.getMessage()).build();
                    userSession.get().getWebSocketSession().sendMessage(new TextMessage(gson.toJson(chatMessageResponse)));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}