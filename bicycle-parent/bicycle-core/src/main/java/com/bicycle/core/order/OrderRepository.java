package com.bicycle.core.order;

public interface OrderRepository {

    void save(Order order);
    
    Order findById(String id);
    
    void deleteById(String id);
    
}
