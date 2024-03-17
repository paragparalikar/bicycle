package com.bicycle.client.kite.adapter;

import com.bicycle.client.kite.adapter.mapper.KiteMapper;
import com.bicycle.client.kite.api.KiteAsyncHttpApi;
import com.bicycle.client.kite.api.KiteDataPublisher;
import com.bicycle.client.kite.api.KiteResilientApi;
import com.bicycle.client.kite.credentials.FileSystemKiteCredentialsRepository;
import com.bicycle.client.kite.credentials.KiteCredentials;
import com.bicycle.client.kite.credentials.KiteCredentialsRepository;
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
public class KiteBrokerClientFactory implements BrokerClientFactory {

    private final KiteDataPublisher kiteDataPublisher;
    private final KiteCredentialsRepository credentialsRepository;
    private final ScheduledExecutorService scheduledExecutorService;
    private final Map<Portfolio, BrokerClient> cache = new HashMap<>();
    
    public KiteBrokerClientFactory() {
        this(null, null);
    }
    
    public KiteBrokerClientFactory(DataPublisher dataPublisher, ScheduledExecutorService scheduledExecutorServic) {
        this(new SimpleKiteDataPublisher(dataPublisher), new FileSystemKiteCredentialsRepository(), scheduledExecutorServic);
    }
    
    @Override
    @Synchronized
    public BrokerClient get(Portfolio portfolio) {
        return cache.computeIfAbsent(portfolio, this::create);
    }
    
    private BrokerClient create(Portfolio portfolio) {
        if(!BrokerType.ZERODHA.equals(portfolio.getBroker())) {
            throw new IllegalArgumentException(String.format(
                    "Portfolio id %s is not a Kite portfolio", 
                    portfolio.getId()));
        }
        final KiteMapper kiteMapper = KiteMapper.INSTANCE;
        final KiteCredentials credentials = credentialsRepository.findByPortfolioId(portfolio.getId());
        final KiteAsyncHttpApi api = new KiteAsyncHttpApi(credentials, kiteDataPublisher, scheduledExecutorService);
        final KiteResilientApi resilientApi = new KiteResilientApi(api);
        return KiteBrokerClient.builder()
                .api(resilientApi)
                .portfolio(portfolio)
                .kiteMapper(kiteMapper)
                .build();
    }
    
    @Override
    public void close() throws Exception {
        for(BrokerClient brokerClient : cache.values()) {
            if(null != brokerClient) brokerClient.close();
        }
    }

}
