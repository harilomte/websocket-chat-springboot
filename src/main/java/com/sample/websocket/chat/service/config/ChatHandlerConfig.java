package com.sample.websocket.chat.service.config;

import com.sample.websocket.chat.api.session.ActiveChatSessions;
import com.sample.websocket.chat.api.session.ActiveUserSessions;
import com.sample.websocket.chat.service.ChatMessageRequestProcessor;
import com.sample.websocket.chat.handler.ChatMessagesHandler;
import com.sample.websocket.chat.service.InitiateChatRequestProcessor;
import com.sample.websocket.chat.service.LoginRequestProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatHandlerConfig {

    @Bean
    public ChatMessagesHandler ChatWebSocketMessagesHandler(
            LoginRequestProcessor loginRequestProcessor,
            InitiateChatRequestProcessor initiateChatRequestProcessor,
            ChatMessageRequestProcessor chatMessageRequestProcessor) {
        return new ChatMessagesHandler(loginRequestProcessor, initiateChatRequestProcessor,
                chatMessageRequestProcessor);

    }
    @Bean
    public LoginRequestProcessor loginRequestHandler(ActiveUserSessions activeUserSessions) {
        return new LoginRequestProcessor(activeUserSessions);
    }

    @Bean
    public InitiateChatRequestProcessor initiateChatRequestHandler(ActiveUserSessions activeUserSessions,
                                                                   ActiveChatSessions activeChatSessions) {
        return new InitiateChatRequestProcessor(activeUserSessions, activeChatSessions);
    }

    @Bean
    public ChatMessageRequestProcessor chatMessageRequestHandler(ActiveUserSessions activeUserSessions,
                                                                 ActiveChatSessions activeChatSessions) {
        return new ChatMessageRequestProcessor(activeUserSessions, activeChatSessions);
    }

    @Bean
    public ActiveUserSessions activeUserSessions() {
        return new ActiveUserSessions();
    }

    @Bean
    public ActiveChatSessions activeChatSessions() {
        return new ActiveChatSessions();
    }
}
