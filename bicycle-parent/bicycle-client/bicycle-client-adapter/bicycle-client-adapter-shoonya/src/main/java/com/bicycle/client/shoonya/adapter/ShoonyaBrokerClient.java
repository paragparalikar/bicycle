package com.bicycle.client.shoonya.adapter;

import com.bicycle.client.shoonya.adapter.mapper.ShoonyaMapper;
import com.bicycle.client.shoonya.api.ShoonyaApi;
import com.bicycle.core.bar.Bar;
import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.bar.provider.query.BarQuery;
import com.bicycle.core.broker.BrokerClient;
import com.bicycle.core.order.Order;
import com.bicycle.core.order.Product;
import com.bicycle.core.portfolio.Portfolio;
import com.bicycle.core.position.Holding;
import com.bicycle.core.position.Position;
import com.bicycle.core.symbol.Segment;
import com.bicycle.core.symbol.Symbol;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
public class ShoonyaBrokerClient implements BrokerClient {

    private final ShoonyaApi api;
    private final Portfolio portfolio;
    private final ShoonyaMapper mapper;
    
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
        api.subscribe(symbols.stream().map(mapper::toShoonyaSymbol).toList());
    }
    
    @Override
    public float getMargin(Segment segment) {
        return api.getMargin(mapper.toShoonyaSegment(segment));
    }

    @Override
    public Order create(Order order) {
        final String id = api.create(mapper.toShoonyaOrder(order));
        order.setId(id);
        return order;
    }

    @Override
    public Order update(Order order) {
        api.update(mapper.toShoonyaOrder(order));
        return order;
    }

    @Override
    public Order cancel(Order order) {
        api.cancel(order.getId());
        return order;
    }

    @Override
    public Collection<Order> findAllOrders() {
        return api.getOrders().stream()
                .map(mapper::toOrder)
                .toList();
    }
    
    @Override
    public Collection<Holding> findAllHoldings() {
        return api.getHoldings(mapper.toShoonyaProduct(Product.CNC)).stream()
                .map(shoonyaHolding -> mapper.toHolding(portfolio.getId(), shoonyaHolding))
                .toList();
    }

    @Override
    public Collection<Position> findAllPositions() {
        return api.getPositions().stream()
                .map(shoonyaPosition -> mapper.toPosition(portfolio.getId(), shoonyaPosition))
                .toList();
    }


    @Override
    public List<Bar> get(BarQuery barQuery) {
        final Symbol symbol = barQuery.symbol();
        final Timeframe timeframe = barQuery.timeframe();
        return api.getBars(
                mapper.toShoonyaSymbol(symbol.exchange(), symbol.code()), 
                mapper.toInterval(timeframe), 
                Date.from(barQuery.from().toInstant()), 
                Date.from(barQuery.to().toInstant())).stream()
            .map(bar -> mapper.toBar(bar, symbol, timeframe))
            .toList();
    }
    
}
