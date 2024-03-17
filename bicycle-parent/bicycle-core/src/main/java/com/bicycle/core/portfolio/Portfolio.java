package com.bicycle.core.portfolio;

import com.bicycle.core.broker.BrokerType;
import lombok.Builder;
import lombok.Data;

@Data
public class Portfolio {
    
    private String id; 
    private float margin;
    private BrokerType broker; 
    private boolean enabledForTrading;
    
    @Builder
    public Portfolio(String id, BrokerType broker, float initialMargin, boolean enabledForTrading) {
        this.id = id;
        this.broker = broker;
        this.margin = initialMargin;
        this.enabledForTrading = enabledForTrading;
    }
    
}
