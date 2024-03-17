package com.bicycle.core.broker;

import com.bicycle.core.portfolio.Portfolio;

public interface BrokerClientFactory extends AutoCloseable {

    BrokerClient get(Portfolio portfolio);
    
}
