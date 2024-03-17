package com.bicycle.core.session;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSessionPublisher implements SessionPublisher, SessionListener {
    
    private final List<SessionListener> sessionListeners = new ArrayList<>();
    
    @Override
    public void subscribe(SessionListener sessionListener) {
        sessionListeners.add(sessionListener);
    }
    
    @Override
    public void onSessionStart(long date) {
        sessionListeners.forEach(listener -> listener.onSessionStart(date));
    }
    
    @Override
    public void onSessionEnd(long date) {
        sessionListeners.forEach(listener -> listener.onSessionEnd(date));
    }

}
