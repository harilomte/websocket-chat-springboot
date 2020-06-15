package com.sample.websocket.chat.handler;

import com.google.gson.Gson;
import com.sample.websocket.chat.api.request.BaseRequest;
import com.sample.websocket.chat.service.ChatMessageRequestProcessor;
import com.sample.websocket.chat.service.InitiateChatRequestProcessor;
import com.sample.websocket.chat.service.LoginRequestProcessor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@AllArgsConstructor
public class ChatMessagesHandler extends TextWebSocketHandler {

    private final Gson gson = new Gson();

    private LoginRequestProcessor loginRequestProcessor;
    private InitiateChatRequestProcessor initiateChatRequestProcessor;
    private ChatMessageRequestProcessor chatMessageRequestProcessor;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("connection established" + session.getId());

        session.sendMessage(new TextMessage("connection established, session: " + session.getId()));
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("connection closed" + session.getId());
        super.afterConnectionClosed(session, status);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        try {
            log.info("received msg:" + message.getPayload() + "session: " + session.getId());

            String payload = message.getPayload();

            // validate request.
            if (StringUtils.isEmpty(payload)) {
                log.info("received empty websocket message, skipping it.");
                return;
            }

            //process request
            BaseRequest request = gson.fromJson(payload, BaseRequest.class);
            if (BaseRequest.RequestType.LOGIN.equals(request.getRequestType())) {
                loginRequestProcessor.process(session, payload);
            }

            if (BaseRequest.RequestType.INITIATE_CHAT.equals(request.getRequestType())) {
                initiateChatRequestProcessor.process(session, payload);
            }

            if (BaseRequest.RequestType.CHAT_MSG.equals(request.getRequestType())) {
                chatMessageRequestProcessor.process(session, payload);
            }
        } catch (Exception e) {
            log.error("exception occurred when handling websocket message", e);
            throw e;
        }
    }
}
