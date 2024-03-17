package com.bicycle.core.session;

import com.bicycle.core.bar.Bar;
import com.bicycle.core.bar.BarListener;

public class MockSessionPublisher extends AbstractSessionPublisher implements BarListener {
    
    private long lastTimestamp;
    
    @Override
    public void init() {
        
    }
    
    @Override
    public void onBar(Bar bar) {
     // More than 12 hours difference ie new trading session
        if(43200000 < bar.date() - lastTimestamp) { 
            if(0 < lastTimestamp) onSessionEnd(lastTimestamp);
            onSessionStart(bar.date());
            lastTimestamp = bar.date();
        }
    }

}
