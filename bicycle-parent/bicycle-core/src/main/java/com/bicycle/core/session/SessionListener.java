package com.bicycle.core.session;

public interface SessionListener {

    void onSessionStart(long date);
    
    void onSessionEnd(long date);
    
}
