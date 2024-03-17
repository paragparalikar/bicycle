package com.bicycle.client.kite.adapter;

import com.bicycle.client.kite.adapter.mapper.KiteMapper;
import com.bicycle.client.kite.api.model.KiteExchange;
import com.bicycle.client.kite.api.symbol.KiteFileSystemSymbolDataLoader;
import com.bicycle.client.kite.api.symbol.KiteSymbolDataLoader;
import com.bicycle.core.symbol.Exchange;
import com.bicycle.core.symbol.Symbol;
import com.bicycle.core.symbol.provider.FilteredSymbolDataProvider;
import com.bicycle.core.symbol.provider.SymbolDataProvider;
import com.bicycle.util.Strings;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class KiteSymbolDataProvider implements SymbolDataProvider {
    
    private final KiteSymbolDataLoader kiteSymbolDataLoader;
    
    public KiteSymbolDataProvider() {
        this(new KiteFileSystemSymbolDataLoader());
    }

    @Override
    @SneakyThrows
    public Collection<Symbol> findByExchange(Exchange exchange) {
        final KiteMapper kiteMapper = KiteMapper.INSTANCE;
        final KiteExchange kiteExchange = kiteMapper.toKiteExchange(exchange);
        return kiteSymbolDataLoader.loadByKiteExchange(kiteExchange).stream()
                .map(kiteMapper::toSymbol)
                .toList();
    }
    
    public SymbolDataProvider equitiesOnly() {
        return new FilteredSymbolDataProvider(this, symbol -> 
            Strings.hasText(symbol.name())
            && !symbol.code().endsWith("BEES")
            && !"INDICES".equalsIgnoreCase(symbol.segment())
            && !symbol.name().endsWith("ETF") 
            && !symbol.name().contains("-"));
    }

}
