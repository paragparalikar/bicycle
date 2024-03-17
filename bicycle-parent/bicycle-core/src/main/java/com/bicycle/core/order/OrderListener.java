package com.bicycle.core.order;

public interface OrderListener {

    void onOrderStatusChanged(Order order);
    
}
