package com.bicycle.client.shoonya.adapter;

import com.bicycle.client.shoonya.adapter.mapper.ShoonyaMapper;
import com.bicycle.client.shoonya.api.model.ShoonyaExchange;
import com.bicycle.client.shoonya.api.symbol.ShoonyaFileSystemSymbolDataLoader;
import com.bicycle.client.shoonya.api.symbol.ShoonyaSymbolDataLoader;
import com.bicycle.core.symbol.Exchange;
import com.bicycle.core.symbol.Symbol;
import com.bicycle.core.symbol.provider.SymbolDataProvider;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class ShoonyaSymbolDataProvider implements SymbolDataProvider {
    
    private final ShoonyaSymbolDataLoader shoonyaSymbolDataLoader;

    public ShoonyaSymbolDataProvider() {
        this(new ShoonyaFileSystemSymbolDataLoader());
    }
    
    @Override
    @SneakyThrows
    public Collection<Symbol> findByExchange(Exchange exchange) {
        final ShoonyaMapper shoonyaMapper = ShoonyaMapper.INSTANCE;
        final ShoonyaExchange shoonyaExchange = shoonyaMapper.toShoonyaExchange(exchange);
        return shoonyaSymbolDataLoader.loadByShoonyaExchange(shoonyaExchange).stream()
                .map(shoonyaMapper::toSymbol)
                .toList();
    }

}
