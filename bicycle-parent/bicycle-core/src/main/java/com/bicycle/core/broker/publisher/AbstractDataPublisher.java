package com.bicycle.core.broker.publisher;

import com.bicycle.core.bar.Bar;
import com.bicycle.core.bar.BarListener;
import com.bicycle.core.order.Order;
import com.bicycle.core.order.OrderListener;
import com.bicycle.core.tick.Tick;
import com.bicycle.core.tick.TickListener;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDataPublisher implements DataPublisher, BarListener, 
    TickListener, OrderListener {
    
    private final List<BarListener> barListeners = new ArrayList<>();
    private final List<TickListener> tickListeners = new ArrayList<>();
    private final List<OrderListener> orderListeners = new ArrayList<>();

    @Override
    public void subscribe(BarListener barListener) {
        barListeners.add(barListener);
    }

    @Override
    public void subscribe(TickListener tickListener) {
        tickListeners.add(tickListener);
    }

    @Override
    public void subscribe(OrderListener orderListener) {
        orderListeners.add(orderListener);
    }
    
    @Override
    public void onOrderStatusChanged(Order order) {
        orderListeners.forEach(listener -> listener.onOrderStatusChanged(order));
    }

    @Override
    public void onTick(Tick tick) {
        tickListeners.forEach(listener -> listener.onTick(tick));
    }

    @Override
    public void onBar(Bar bar) {
        barListeners.forEach(listener -> listener.onBar(bar));
    }

}
