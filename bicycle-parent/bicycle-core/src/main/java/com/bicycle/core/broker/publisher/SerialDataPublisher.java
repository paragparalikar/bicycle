package com.bicycle.core.broker.publisher;

import com.bicycle.core.bar.Bar;
import com.bicycle.core.bar.BarReader;
import com.bicycle.core.order.Order;
import com.bicycle.core.tick.Tick;
import com.bicycle.core.tick.TickReader;

public class SerialDataPublisher extends AbstractDataPublisher {
    
    private final Bar bar = new Bar();
    private final Tick tick = new Tick();

    @Override
    public void init() {

    }
    
    @Override
    public void close() throws Exception {

    }

    @Override
    public void publish(TickReader tickReader) {
        for(int index = 0; index < tickReader.size(); index++) {
            tickReader.readInto(tick);
            onTick(tick);
        }
    }

    @Override
    public void publish(BarReader barReader) {
        for(int index = 0; index < barReader.size(); index++) {
            barReader.readInto(bar);
            onBar(bar);
        }
    }
    
    @Override
    public void publish(Order order) {
        onOrderStatusChanged(order);
    }

}
