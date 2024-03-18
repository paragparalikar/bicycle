package com.bicycle.client.kite.data;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.bicycle.client.kite.adapter.KiteBrokerClientFactory;
import com.bicycle.client.kite.adapter.KiteSymbolDataProvider;
import com.bicycle.core.bar.Bar;
import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.bar.provider.BarDataProvider;
import com.bicycle.core.bar.provider.LoadBalancedBarDataProvider;
import com.bicycle.core.bar.provider.query.BarQuery;
import com.bicycle.core.bar.repository.BarRepository;
import com.bicycle.core.bar.repository.FileSystemBarRepository;
import com.bicycle.core.portfolio.FileSystemPortfolioRepository;
import com.bicycle.core.portfolio.PortfolioRepository;
import com.bicycle.core.symbol.Exchange;
import com.bicycle.core.symbol.Symbol;
import com.bicycle.core.symbol.provider.SymbolDataProvider;
import com.bicycle.core.symbol.repository.CacheSymbolRepository;
import com.bicycle.core.symbol.repository.SymbolRepository;
import com.bicycle.util.Dates;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
public class KiteSymbolDataDownloader implements Runnable {
	
	public static void main(String[] args) throws InterruptedException {
		final KiteBrokerClientFactory brokerClientFactory = new KiteBrokerClientFactory();
		final PortfolioRepository portfolioRepository = new FileSystemPortfolioRepository();
		final BarDataProvider barDataProvider = new LoadBalancedBarDataProvider(portfolioRepository, brokerClientFactory);
		final SymbolDataProvider symbolDataProvider = new KiteSymbolDataProvider().equitiesOnly();
		final SymbolRepository symbolRepository = new CacheSymbolRepository(symbolDataProvider);
		final BarRepository barRepository = new FileSystemBarRepository(symbolRepository);
		final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
		
		for(Timeframe timeframe : Arrays.asList(Timeframe.values()).reversed()) {
			for(Symbol symbol : symbolRepository.findByExchange(Exchange.NSE)) {
				executorService.execute(KiteSymbolDataDownloader.builder()
						.symbol(symbol)
						.timeframe(timeframe)
						.barRepository(barRepository)
						.barDataProvider(barDataProvider)
						.build());
			}
		}
		
		executorService.shutdown();
		executorService.awaitTermination(10, TimeUnit.HOURS);
	}
	
	private final Symbol symbol;
	private final Timeframe timeframe;
	private final BarRepository barRepository;
	private final BarDataProvider barDataProvider;

	@Override
	public void run() {
		final ZonedDateTime lastDownloadDate = Dates.toZonedDateTime(barRepository.getLastDownloadDate(symbol, timeframe));
		final ZonedDateTime fromDate = lastDownloadDate.plusMinutes(timeframe.getMinuteMultiple());
		final ZonedDateTime toDate = ZonedDateTime.now();
		final BarQuery barQuery = BarQuery.builder()
				.symbol(symbol)
				.timeframe(timeframe)
				.from(fromDate)
				.to(toDate)
				.build();
		final List<Bar> bars = barDataProvider.get(barQuery);
		barRepository.persist(symbol, timeframe, bars);
		System.out.printf("%-5s %-30s %d\n", timeframe, symbol.code(), bars.size());
	}

}
