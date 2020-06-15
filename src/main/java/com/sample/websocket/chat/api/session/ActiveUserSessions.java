package com.sample.websocket.chat.api.session;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
public class ActiveUserSessions {

    private List<UserSession> userSessions = new ArrayList<>();

    public void addUserSession(UserSession userSession) {
        userSessions.add(userSession);
    }

    public void removeUserSession(UserSession userSession) {
        userSessions.remove(userSession);
    }

    public List<String> getAllUserNames() {
        return userSessions.stream().map(UserSession::getUser).collect(Collectors.toList());
    }

    public Optional<UserSession> getUserSessionById(String sessionId) {

        return userSessions.stream().filter(userSession -> userSession.getSessionId().equals(sessionId)).findFirst();

    }

    public Optional<UserSession> getUserSessionByName(String userName) {
        return userSessions.stream().filter(userSession -> userSession.getUser().equals(userName)).findFirst();
    }
}
