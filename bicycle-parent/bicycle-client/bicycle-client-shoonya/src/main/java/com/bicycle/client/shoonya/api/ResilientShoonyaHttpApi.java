package com.bicycle.client.shoonya.api;

import java.time.Duration;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

import com.bicycle.client.shoonya.api.model.ShoonyaBar;
import com.bicycle.client.shoonya.api.model.ShoonyaHolding;
import com.bicycle.client.shoonya.api.model.ShoonyaOrder;
import com.bicycle.client.shoonya.api.model.ShoonyaPosition;
import com.bicycle.client.shoonya.api.model.ShoonyaSymbol;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class ResilientShoonyaHttpApi implements ShoonyaApi {
    private static final String DEFAULT_RETRY = "default-retry";
    private static final String PER_MINUTE_RATE_LIMITER = "per-minute-rate-limiter";
    private static final String PER_SECOND_RATE_LIMITER = "per-second-rate-limiter";

    private final ShoonyaApi delegate;
    private final RateLimiter perSecondRateLimiter = RateLimiter.of(PER_SECOND_RATE_LIMITER, RateLimiterConfig.custom()
            .limitForPeriod(20)
            .limitRefreshPeriod(Duration.ofSeconds(1))
            .timeoutDuration(Duration.ofMinutes(3))
            .build());
    private final RateLimiter perMinuteRateLimite = RateLimiter.of(PER_MINUTE_RATE_LIMITER, RateLimiterConfig.custom()
            .limitForPeriod(200)
            .limitRefreshPeriod(Duration.ofMinutes(1))
            .timeoutDuration(Duration.ofMinutes(3))
            .build());
    private final Retry retry = Retry.of(DEFAULT_RETRY, RetryConfig.custom()
            .maxAttempts(5)
            .failAfterMaxAttempts(true)
            .waitDuration(Duration.ofMillis(1000))
            .retryOnException(this::shouldRetry)
            .build());
    
    @Override
    public void init() {
        delegate.init();
    }
    
    @Override
    public void close() throws Exception {
        delegate.close();
    }
    
    @Override
    public void subscribe(Collection<ShoonyaSymbol> symbols) {
        delegate.subscribe(symbols);
    }
    
    @SneakyThrows
    private <T> T execute(Supplier<T> function, boolean retry){
        function = RateLimiter.decorateSupplier(perSecondRateLimiter, function);
        function = RateLimiter.decorateSupplier(perMinuteRateLimite, function);
        if(retry) function = Retry.decorateSupplier(this.retry, function);
        return function.get();
    }
    
    @SneakyThrows
    private void execute(Runnable runnable){
        runnable = RateLimiter.decorateRunnable(perSecondRateLimiter, runnable);
        runnable = RateLimiter.decorateRunnable(perMinuteRateLimite, runnable);
        runnable = Retry.decorateRunnable(retry, runnable);
        runnable.run();
    }
    
    private boolean shouldRetry(Throwable throwable) {
        if(throwable instanceof ShoonyaHttpClientException) {
            final int statusCode = ShoonyaHttpClientException.class.cast(throwable).getStatusCode();
            return !(statusCode >= 400 && statusCode < 500);
        }
        return true;
    }
    
    @Override
    public List<ShoonyaBar> getBars(ShoonyaSymbol symbol, int interval, Date startDate, Date endDate) {
        return execute(() -> delegate.getBars(symbol, interval, startDate, endDate), false);
    }
    
    @Override
    public String create(ShoonyaOrder order) {
        return execute(() -> delegate.create(order), true);
    }
    
    @Override
    public void update(ShoonyaOrder order) {
        execute(() -> delegate.update(order));
    }
    
    @Override
    public void cancel(String orderId) {
        execute(() -> delegate.cancel(orderId));
    }

    @Override
    public List<ShoonyaOrder> getOrders() {
        return execute(() -> delegate.getOrders(), true);
    }

    @Override
    public List<ShoonyaPosition> getPositions() {
        return execute(() -> delegate.getPositions(), true);
    }

    @Override
    public List<ShoonyaHolding> getHoldings(String product) {
        return execute(() -> delegate.getHoldings(product), true);
    }

    @Override
    public float getMargin(String segment) {
        return execute(() -> delegate.getMargin(segment), true);
    }

}
