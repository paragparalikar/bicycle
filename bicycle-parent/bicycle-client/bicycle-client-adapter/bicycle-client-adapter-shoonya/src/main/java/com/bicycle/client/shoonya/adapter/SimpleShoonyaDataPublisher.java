package com.bicycle.client.shoonya.adapter;

import com.bicycle.client.shoonya.adapter.mapper.ShoonyaMapper;
import com.bicycle.client.shoonya.api.ShoonyaDataPublisher;
import com.bicycle.client.shoonya.api.model.ShoonyaOrder;
import com.bicycle.client.shoonya.api.model.ShoonyaTick;
import com.bicycle.core.broker.publisher.DataPublisher;
import com.bicycle.core.tick.TickReader;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SimpleShoonyaDataPublisher implements ShoonyaDataPublisher {

    private final DataPublisher dataPublisher;
    
    @Override
    public void publish(ShoonyaTick shoonyaTick) {
        final ShoonyaMapper shoonyaMapper = ShoonyaMapper.INSTANCE;
        dataPublisher.publish(TickReader.of(shoonyaMapper.toTick(shoonyaTick)));
    }

    @Override
    public void publish(ShoonyaOrder shoonyaOrder) {
        final ShoonyaMapper shoonyaMapper = ShoonyaMapper.INSTANCE;
        dataPublisher.publish(shoonyaMapper.toOrder(shoonyaOrder));
    }

}
