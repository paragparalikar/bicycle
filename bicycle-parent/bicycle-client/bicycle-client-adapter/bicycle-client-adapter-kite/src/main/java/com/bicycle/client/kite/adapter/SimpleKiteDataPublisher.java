package com.bicycle.client.kite.adapter;

import com.bicycle.client.kite.adapter.mapper.KiteMapper;
import com.bicycle.client.kite.api.KiteDataPublisher;
import com.bicycle.client.kite.api.model.KiteOrder;
import com.bicycle.client.kite.api.model.KiteTick;
import com.bicycle.core.broker.publisher.DataPublisher;
import com.bicycle.core.tick.TickReader;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SimpleKiteDataPublisher implements KiteDataPublisher {

    private final DataPublisher delegate;
    
    @Override public void publish(KiteTick kiteTick) {
        final KiteMapper kiteMapper = KiteMapper.INSTANCE;
        delegate.publish(TickReader.of(kiteMapper.toTick(kiteTick)));
    }
    
    @Override public void publish(KiteOrder kiteOrder) {
        final KiteMapper kiteMapper = KiteMapper.INSTANCE;
        delegate.publish(kiteMapper.toOrder(kiteOrder));
    }
    
}
