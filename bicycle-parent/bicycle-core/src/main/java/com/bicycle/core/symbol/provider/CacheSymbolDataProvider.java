package com.bicycle.core.symbol.provider;

import com.bicycle.core.symbol.Exchange;
import com.bicycle.core.symbol.Symbol;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CacheSymbolDataProvider implements SymbolDataProvider {

    private final SymbolDataProvider delegate;
    private final Map<Exchange, Collection<Symbol>> cache = new HashMap<>();
    
    @Override
    public Collection<Symbol> findByExchange(Exchange exchange) {
        return cache.computeIfAbsent(exchange, delegate::findByExchange);
    }

}
