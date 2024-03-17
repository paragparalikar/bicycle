package com.bicycle.client.kite.api;

import com.bicycle.client.kite.api.model.KiteCandle;
import com.bicycle.client.kite.api.model.KiteHolding;
import com.bicycle.client.kite.api.model.KiteInterval;
import com.bicycle.client.kite.api.model.KiteMargin;
import com.bicycle.client.kite.api.model.KiteOrder;
import com.bicycle.client.kite.api.model.KiteOrderId;
import com.bicycle.client.kite.api.model.KitePosition;
import com.bicycle.client.kite.api.model.KiteProfile;
import com.bicycle.client.kite.api.model.KiteQuote;
import com.bicycle.client.kite.api.model.KiteQuoteMode;
import com.bicycle.client.kite.api.model.KiteSymbol;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KiteResilientApi implements KiteApi {

	@NonNull private final KiteApi delegate;
	
	private final RateLimiter quoteRateLimiter = RateLimiter.of("quote", RateLimiterConfig.custom()
	        .limitForPeriod(1)
	        .limitRefreshPeriod(Duration.ofSeconds(1))
	        .timeoutDuration(Duration.ofMinutes(1))
	        .build());
	private final RateLimiter candleDataRateLimiter = RateLimiter.of("candle-data", RateLimiterConfig.custom()
            .limitForPeriod(3)
            .limitRefreshPeriod(Duration.ofSeconds(1))
            .timeoutDuration(Duration.ofHours(6))
            .build());
	private final RateLimiter orderRateLimiter = RateLimiter.of("order", RateLimiterConfig.custom()
            .limitForPeriod(10)
            .limitRefreshPeriod(Duration.ofSeconds(1))
            .timeoutDuration(Duration.ofMinutes(1))
            .build());
	private final RateLimiter othersRateLimiter = RateLimiter.of("other", RateLimiterConfig.custom()
            .limitForPeriod(10)
            .limitRefreshPeriod(Duration.ofSeconds(1))
            .timeoutDuration(Duration.ofMinutes(1))
            .build());
	private final RateLimiter allRateLimiter = RateLimiter.of("all", RateLimiterConfig.custom()
            .limitForPeriod(200)
            .limitRefreshPeriod(Duration.ofMinutes(1))
            .timeoutDuration(Duration.ofHours(6))
            .build());
	private final Retry retry = Retry.of("retry", RetryConfig.custom()
            .maxAttempts(3)
            .failAfterMaxAttempts(true)
            .waitDuration(Duration.ofMillis(1000))
            .build());
	
	
	private <T> T execute(Supplier<T> supplier, RateLimiter rateLimiter, boolean retry) {
	    supplier = RateLimiter.decorateSupplier(rateLimiter, supplier);
	    supplier = RateLimiter.decorateSupplier(allRateLimiter, supplier);
	    if(retry) supplier = Retry.decorateSupplier(this.retry, supplier);
	    return supplier.get();
	}
	
	@Override
	public void init() {
	    delegate.init();
	}
	
	@Override
	public List<KiteCandle> getData(KiteSymbol symbol, KiteInterval interval, ZonedDateTime from, ZonedDateTime to) {
		return execute(() -> delegate.getData(symbol, interval, from, to), candleDataRateLimiter, false);
	}

	@Override
	public KiteProfile getProfile() {
		return execute(delegate::getProfile, othersRateLimiter, true);
	}

	@Override
	public KiteMargin getMargin() {
	    return execute(delegate::getMargin, othersRateLimiter, true);
	}

	@Override
	public List<KiteHolding> getHoldings() {
	    return execute(delegate::getHoldings, othersRateLimiter, true);
	}

	@Override
	public List<KitePosition> getPositions() {
	    return execute(delegate::getPositions, othersRateLimiter, true);
	}

	@Override
	public List<KiteOrder> getOrders() {
	    return execute(delegate::getOrders, othersRateLimiter, true);
	}

	@Override
	public KiteOrderId create(KiteOrder order) {
		return execute(() -> delegate.create(order), orderRateLimiter, true);
	}

	@Override
	public KiteOrderId update(KiteOrder order) {
	    return execute(() -> delegate.update(order), orderRateLimiter, true);
	}

	@Override
	public KiteOrderId cancel(KiteOrder order) {
		return execute(() -> delegate.cancel(order), orderRateLimiter, true);
	}
	
	@Override
	public Collection<KiteQuote> getQuotes(Collection<KiteSymbol> instruments, KiteQuoteMode mode) {
		return execute(() -> delegate.getQuotes(instruments, mode), quoteRateLimiter, true);
	}
	
	@Override
	public void subscribe(Collection<KiteSymbol> symbols) {
	    delegate.subscribe(symbols);
	}
	
	@Override
	public void close() throws Exception {
		delegate.close();
	}
	
}
