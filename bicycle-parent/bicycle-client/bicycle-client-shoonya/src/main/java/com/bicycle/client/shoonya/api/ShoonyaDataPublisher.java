package com.bicycle.client.shoonya.api;

import com.bicycle.client.shoonya.api.model.ShoonyaOrder;
import com.bicycle.client.shoonya.api.model.ShoonyaTick;

public interface ShoonyaDataPublisher {
    
    void publish(ShoonyaTick shoonyaTick);
    
    void publish(ShoonyaOrder shoonyaOrder);

}
