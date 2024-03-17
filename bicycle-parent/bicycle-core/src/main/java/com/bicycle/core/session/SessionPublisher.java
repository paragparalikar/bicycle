package com.bicycle.core.session;

public interface SessionPublisher {
    
    void init();
    
    void subscribe(SessionListener sessionListener);

}
