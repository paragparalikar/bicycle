package com.bicycle.client.kite.adapter;

import com.bicycle.client.kite.adapter.mapper.KiteMapper;
import com.bicycle.client.kite.api.KiteApi;
import com.bicycle.client.kite.api.model.KiteCandle;
import com.bicycle.client.kite.api.model.KiteInterval;
import com.bicycle.client.kite.api.model.KiteSymbol;
import com.bicycle.core.bar.Bar;
import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.bar.provider.query.BarQuery;
import com.bicycle.core.broker.BrokerClient;
import com.bicycle.core.order.Order;
import com.bicycle.core.portfolio.Portfolio;
import com.bicycle.core.position.Holding;
import com.bicycle.core.position.Position;
import com.bicycle.core.symbol.Segment;
import com.bicycle.core.symbol.Symbol;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
public class KiteBrokerClient implements BrokerClient {

    private final KiteApi api;
    private final Portfolio portfolio;
    private final KiteMapper kiteMapper;
    
    @Override
    public void init() { 
        api.init();
    }
    
    @Override
    public void close() throws Exception {
        if(null != api) api.close();
    }
    
    @Override
    public void subscribe(Collection<Symbol> symbols) {
        api.subscribe(symbols.stream().map(kiteMapper::getKiteSymbol).toList());
    }
    
    @Override
    public float getMargin(Segment segment) {
        switch(segment) {
            case CM: return (float) api.getMargin().getEquity().getAvailable().getCash();
            case MCX: return (float) api.getMargin().getCommodity().getAvailable().getCash();
            case FO: 
            case FX:
            default: throw new IllegalArgumentException(
                    String.format("Segment %s is not supporeted by kite", segment.name()));
        }
    }

    @Override
    public Order create(Order order) {
        final String id = api.create(kiteMapper.toKiteOrder(order)).getOrderId();
        order.setId(id);
        return order;
    }

    @Override
    public Order update(Order order) {
        api.update(kiteMapper.toKiteOrder(order));
        return order;
    }

    @Override
    public Order cancel(Order order) {
        api.cancel(kiteMapper.toKiteOrder(order));
        return order;
    }

    @Override
    public Collection<Order> findAllOrders() {
        return api.getOrders().stream()
                .map(kiteMapper::toOrder)
                .collect(Collectors.toSet());
    }
    
    @Override
    public Collection<Holding> findAllHoldings() {
        return api.getHoldings().stream()
                .map(holding -> kiteMapper.toHolding(portfolio.getId(), holding))
                .toList();
    }

    @Override
    public Collection<Position> findAllPositions() {
        return api.getPositions().stream()
                .map(kitePosition -> kiteMapper.toPosition(portfolio.getId(), kitePosition))
                .toList();
    }
    
    @Override
    public List<Bar> get(BarQuery barQuery) {
        final Symbol symbol = barQuery.symbol();
        final Timeframe timeframe = barQuery.timeframe();
        return getData(
                kiteMapper.getKiteSymbol(symbol.exchange(), symbol.code()), 
                kiteMapper.toKiteInterval(timeframe), 
                barQuery.from(), 
                barQuery.to()).stream()
            .map(kiteCandle -> kiteMapper.toBar(symbol, timeframe, kiteCandle))
            .sorted(Comparator.comparing(Bar::date))
            .toList();
    }
    
    private List<KiteCandle> getData(KiteSymbol symbol, KiteInterval interval, ZonedDateTime from, ZonedDateTime to) {
        final List<KiteCandle> candles = new ArrayList<>();
        while(null != to && to.isAfter(from)) {
            final ZonedDateTime projectedFrom = to.minus(Duration.ofDays(interval.getHistoricalBatchLimitInDays()));
            final ZonedDateTime effectiveFrom = from.isAfter(projectedFrom) ? from : projectedFrom;
            final List<KiteCandle> subCandles = api.getData(symbol, interval, effectiveFrom, to);
            if(null != subCandles && !subCandles.isEmpty()) {
                candles.addAll(subCandles);
                to = subCandles.get(0).getTimestamp().minus(interval.getDuration());
            } else {
                to = null;
            }
        }
        return candles;
    }
    
    

}
