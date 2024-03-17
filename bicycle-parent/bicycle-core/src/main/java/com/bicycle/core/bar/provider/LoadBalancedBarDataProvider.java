package com.bicycle.core.bar.provider;

import com.bicycle.core.bar.Bar;
import com.bicycle.core.bar.provider.query.BarQuery;
import com.bicycle.core.broker.BrokerClient;
import com.bicycle.core.broker.BrokerClientFactory;
import com.bicycle.core.broker.BrokerType;
import com.bicycle.core.portfolio.Portfolio;
import com.bicycle.core.portfolio.PortfolioRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
public class LoadBalancedBarDataProvider implements BarDataProvider {

    private final PortfolioRepository portfolioRepository;
    private final BrokerClientFactory brokerClientFactory;
    private final AtomicInteger position = new AtomicInteger(0);
    private final List<Portfolio> portfolios = new ArrayList<>();
    
    private void load() {
        if(portfolios.isEmpty()) {
            synchronized(this) {
                if(portfolios.isEmpty()) {
                    portfolioRepository.findAll().stream()
                            .filter(portfolio -> BrokerType.ZERODHA.equals(portfolio.getBroker()))
                            .sorted(Comparator.comparing(Portfolio::getId))
                            .forEach(portfolios::add);
                }
            }
        }
    }

    @Override
    public List<Bar> get(BarQuery barQuery) {
        load();
        final int pos = position.getAndUpdate(value -> value >= portfolios.size() - 1 ? 0 : value + 1);
        final BrokerClient brokerClient = brokerClientFactory.get(portfolios.get(pos));
        return brokerClient.get(barQuery);
    }

}
