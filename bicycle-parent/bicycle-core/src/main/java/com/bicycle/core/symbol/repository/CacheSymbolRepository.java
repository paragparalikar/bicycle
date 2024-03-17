package com.bicycle.core.symbol.repository;

import com.bicycle.core.symbol.Exchange;
import com.bicycle.core.symbol.Symbol;
import com.bicycle.core.symbol.provider.SymbolDataProvider;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

@RequiredArgsConstructor
public class CacheSymbolRepository implements SymbolRepository {
    
    private final SymbolDataProvider symbolDataProvider;
    private final Map<Exchange, Map<String, Symbol>> exchangeCodeSymbolCache = new HashMap<>();
    private final Int2ObjectOpenHashMap<Symbol> tokenSymbolCache = new Int2ObjectOpenHashMap<>(2048); // TODO ignoring exchange
    
    private boolean isEmpty(Exchange exchange) {
        return tokenSymbolCache.isEmpty() || exchangeCodeSymbolCache.isEmpty();
    }
    
    private void loadIfEmpty(Exchange exchange) {
        if(isEmpty(exchange)) { synchronized(tokenSymbolCache) { if(isEmpty(exchange)) {
            for(Symbol symbol : symbolDataProvider.findByExchange(exchange)) {
                exchangeCodeSymbolCache.computeIfAbsent(exchange, 
                        key -> new HashMap<>()).put(symbol.code(), symbol);
                tokenSymbolCache.put(symbol.token(), symbol);
            }
        } } }
    }
    
    @Override
    public Collection<Symbol> findByExchange(Exchange exchange) {
        loadIfEmpty(exchange);
        return tokenSymbolCache.values();   // TODO ignoring exchange
    }
    
    @Override
    public Symbol findByToken(int token, Exchange exchange) {
        loadIfEmpty(exchange);
        return tokenSymbolCache.get(token);
    }

    @Override
    public Symbol findByCode(String code, Exchange exchange) {
        loadIfEmpty(exchange);
        return exchangeCodeSymbolCache.getOrDefault(exchange, 
                Collections.emptyMap()).get(code);
    }
    
}
