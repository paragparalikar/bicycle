package com.bicycle.core.bar.downloader;

import com.bicycle.core.bar.Bar;
import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.bar.provider.BarDataProvider;
import com.bicycle.core.bar.provider.query.BarQuery;
import com.bicycle.core.bar.provider.query.BarQueryTransformer;
import com.bicycle.core.bar.repository.BarRepository;
import com.bicycle.core.symbol.Exchange;
import com.bicycle.core.symbol.Symbol;
import com.bicycle.core.symbol.repository.SymbolRepository;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Builder
@RequiredArgsConstructor
public class BarDataDownloader {
    
    private final BarRepository barRepository;
    private final BarDataProvider barDataProvider;
    private final SymbolRepository symbolRepository;
    private final BarQueryTransformer queryTransformer;
    
    
    @SneakyThrows
    public void download() {
        final ExecutorService executorService = Executors.newFixedThreadPool(20);
        for(Symbol symbol : symbolRepository.findByExchange(Exchange.NSE)) {
            for(Timeframe timeframe : Arrays.asList(Timeframe.D)) { // TODO all timeframes
                executorService.execute(() -> download(symbol, timeframe));
            }
        }
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.HOURS);
    }
    
    private void download(Symbol symbol, Timeframe timeframe) {
        try {
            final long ldd = barRepository.getLastDownloadDate(symbol, timeframe);
            final ZonedDateTime from = ZonedDateTime.ofInstant(Instant.ofEpochMilli(ldd), 
                    ZoneId.systemDefault()).plusMinutes(timeframe.getMinuteMultiple());
            final ZonedDateTime to = ZonedDateTime.now();
            queryTransformer.transform(BarQuery.builder()
                    .symbol(symbol).timeframe(timeframe)
                    .to(to).from(from).build())
            .ifPresent(this::download);
        } catch(Exception e) {
            //e.printStackTrace();
            System.err.println(symbol.code() + "," + 0);
        }
    }
    
    private void download(BarQuery barQuery) {
        final List<Bar> bars = barDataProvider.get(barQuery);
        barRepository.persist(bars);
        System.out.println(barQuery.symbol().code() + "," + bars.size());
        //System.out.printf("Downloaded %d bars for %s - %s\n", bars.size(), 
        //        barQuery.symbol().code(), barQuery.timeframe().name());
    }
    
}
