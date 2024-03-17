package com.bicycle.client.shoonya.adapter.mapper;

import com.bicycle.client.shoonya.api.model.ShoonyaExchange;
import com.bicycle.client.shoonya.api.model.ShoonyaSymbol;
import com.bicycle.core.symbol.Exchange;
import com.bicycle.core.symbol.Symbol;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.RequiredArgsConstructor;


@Builder
@RequiredArgsConstructor
public class ShoonyaSymbolMapper {

    private final ShoonyaExchangeMapper shoonyaExchangeMapper;
    private final Map<ShoonyaExchange, Map<String, Symbol>> symbolCache = new HashMap<>();
    private final Map<Exchange, Map<String, ShoonyaSymbol>> shoonyaSymbolCache = new HashMap<>();
    private final Map<ShoonyaExchange, Map<Integer, Symbol>> exchangeCodeSymbolCache = new HashMap<>();
    
    public Symbol toSymbol(ShoonyaSymbol shoonyaSymbol) {
        if(null == shoonyaSymbol) return null;
        final Symbol symbol = Symbol.builder()
                .type(shoonyaSymbol.getType())
                .code(shoonyaSymbol.getCode())
                .name(shoonyaSymbol.getName())
                .token(shoonyaSymbol.getToken())
                .lotSize(shoonyaSymbol.getLotSize())
                .tickSize(shoonyaSymbol.getTickSize())
                .exchange(shoonyaExchangeMapper.toExchange(shoonyaSymbol.getExchange()))
                .build();
        cache(symbol.exchange(), symbol.name(), shoonyaSymbol);
        cache(shoonyaSymbol.getExchange(), shoonyaSymbol.getName(), symbol);
        cache(shoonyaSymbol.getExchange(), shoonyaSymbol.getToken(), symbol);
        return symbol;
    }
    
    private void cache(ShoonyaExchange shoonyaExchange, int exchangeToken, Symbol symbol) {
        exchangeCodeSymbolCache.computeIfAbsent(shoonyaExchange, key -> new HashMap<>()).put(exchangeToken, symbol);
    }
    
    public Symbol getSymbol(ShoonyaExchange shoonyaExchange, int exchangeToken) {
        return exchangeCodeSymbolCache.getOrDefault(shoonyaExchange, Collections.emptyMap()).get(exchangeToken);
    }
    
    private void cache(ShoonyaExchange exchange, String symbolName, Symbol symbol) {
        symbolCache.computeIfAbsent(exchange, key -> new HashMap<>()).put(symbolName, symbol);
    }
    
    public Symbol toSymbol(ShoonyaExchange exchange, String symbolName) {
        return symbolCache.getOrDefault(exchange, Collections.emptyMap()).get(symbolName);
    }
    
    private void cache(Exchange exchange, String code, ShoonyaSymbol shoonyaSymbol) {
        shoonyaSymbolCache.computeIfAbsent(exchange, key -> new HashMap<>()).put(code, shoonyaSymbol);
    }
    
    public ShoonyaSymbol toShoonyaSymbol(Exchange exchange, String name) {
        return shoonyaSymbolCache.getOrDefault(exchange, Collections.emptyMap()).get(name);
    }
    
    public ShoonyaSymbol toShoonyaSymbol(Symbol symbol) {
        return toShoonyaSymbol(symbol.exchange(), symbol.name());
    }
}

