package com.bicycle.core.broker.publisher;

import com.bicycle.core.bar.BarListener;
import com.bicycle.core.bar.BarReader;
import com.bicycle.core.order.Order;
import com.bicycle.core.order.OrderListener;
import com.bicycle.core.tick.TickListener;
import com.bicycle.core.tick.TickReader;

public interface DataPublisher extends AutoCloseable {
    
    void init();
    
    
    void publish(Order order);
    
    void publish(BarReader barReader);

    void publish(TickReader tickReader);
    

    void subscribe(BarListener barListener);
    
    void subscribe(TickListener tickListener);
    
    void subscribe(OrderListener orderListener);

}
