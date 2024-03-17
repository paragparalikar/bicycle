package com.bicycle.client.shoonya.adapter;

import com.bicycle.client.shoonya.adapter.mapper.ShoonyaMapper;
import com.bicycle.client.shoonya.api.ResilientShoonyaHttpApi;
import com.bicycle.client.shoonya.api.ShoonyaApi;
import com.bicycle.client.shoonya.api.ShoonyaAsyncHttpApi;
import com.bicycle.client.shoonya.api.ShoonyaDataPublisher;
import com.bicycle.client.shoonya.credentials.ShoonyaCredentialRepository;
import com.bicycle.client.shoonya.credentials.ShoonyaCredentials;
import com.bicycle.client.shoonya.credentials.ShoonyaFileSystemCredentialRepository;
import com.bicycle.core.broker.BrokerClient;
import com.bicycle.core.broker.BrokerClientFactory;
import com.bicycle.core.broker.BrokerType;
import com.bicycle.core.broker.publisher.DataPublisher;
import com.bicycle.core.portfolio.Portfolio;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;

@Builder
@RequiredArgsConstructor
public class ShoonyaBrokerClientFactory implements BrokerClientFactory {
    
    private final ShoonyaDataPublisher shoonyaDataPublisher;
    private final ScheduledExecutorService scheduledExecutorService;
    private final Map<Portfolio, BrokerClient> cache = new HashMap<>();
    private final ShoonyaCredentialRepository shoonyaCredentialsRepository;
    
    public ShoonyaBrokerClientFactory(DataPublisher dataPublisher, ScheduledExecutorService scheduledExecutorService) {
        this(new SimpleShoonyaDataPublisher(dataPublisher), scheduledExecutorService, new ShoonyaFileSystemCredentialRepository());
    }

    @Override
    @Synchronized
    public BrokerClient get(Portfolio portfolio) {
        return cache.computeIfAbsent(portfolio, this::create);
    }
    
    private BrokerClient create(Portfolio portfolio) {
        if(!BrokerType.FINVASIA.equals(portfolio.getBroker())) {
            throw new IllegalArgumentException(String.format(
                    "Portfolio id %s is not a Shoonya portfolio", 
                    portfolio.getId()));
        }
        final ShoonyaCredentials credentials = shoonyaCredentialsRepository
                .findByPortfolioId(portfolio.getId());
        final ShoonyaMapper shoonyaMapper = ShoonyaMapper.INSTANCE;
        final ShoonyaApi api = new ShoonyaAsyncHttpApi(credentials, shoonyaDataPublisher, scheduledExecutorService);
        final ShoonyaApi resilientApi = new ResilientShoonyaHttpApi(api);
        return ShoonyaBrokerClient.builder()
                .api(resilientApi)
                .portfolio(portfolio)
                .mapper(shoonyaMapper)
                .build();
    }
    
    @Override
    public void close() throws Exception {
        for(BrokerClient brokerClient : cache.values()) {
            if(null != brokerClient) brokerClient.close();
        }
    }

}
