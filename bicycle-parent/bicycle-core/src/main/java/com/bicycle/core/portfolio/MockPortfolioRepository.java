package com.bicycle.core.portfolio;

import com.bicycle.core.broker.BrokerType;
import java.util.Collection;
import java.util.Collections;

public class MockPortfolioRepository implements PortfolioRepository {
    
    private final Collection<Portfolio> portfolios;
    
    public MockPortfolioRepository(float initialMargin) {
        portfolios = Collections.singleton(Portfolio.builder()
                .enabledForTrading(true)
                .broker(BrokerType.MOCK)
                .id(BrokerType.MOCK.name())
                .initialMargin(initialMargin)
                .build());
    }

    @Override
    public Collection<Portfolio> findAll() {
        return portfolios;
    }

}
