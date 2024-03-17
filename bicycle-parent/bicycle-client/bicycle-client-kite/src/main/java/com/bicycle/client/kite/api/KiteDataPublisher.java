package com.bicycle.client.kite.api;

import com.bicycle.client.kite.api.model.KiteOrder;
import com.bicycle.client.kite.api.model.KiteTick;

public interface KiteDataPublisher {

    void publish(KiteTick kiteTick);

    void publish(KiteOrder kiteOrder);

}