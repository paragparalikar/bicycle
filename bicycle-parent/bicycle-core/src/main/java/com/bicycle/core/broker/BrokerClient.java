package com.bicycle.core.broker;

import com.bicycle.core.bar.provider.BarDataProvider;
import com.bicycle.core.order.Order;
import com.bicycle.core.position.Holding;
import com.bicycle.core.position.Position;
import com.bicycle.core.symbol.Segment;
import com.bicycle.core.symbol.Symbol;
import java.util.Collection;

public interface BrokerClient extends BarDataProvider, AutoCloseable {
    
    void init();
    
    Order create(Order order);
    
    Order update(Order order);
     
    Order cancel(Order order);
    
    float getMargin(Segment segment);
    
    Collection<Order> findAllOrders();
    
    Collection<Holding> findAllHoldings();
    
    Collection<Position> findAllPositions();
    
    void subscribe(Collection<Symbol> symbols);

}
