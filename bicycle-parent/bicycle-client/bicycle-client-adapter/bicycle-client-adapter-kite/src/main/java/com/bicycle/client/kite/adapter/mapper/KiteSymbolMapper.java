package com.bicycle.client.kite.adapter.mapper;

import com.bicycle.client.kite.api.model.KiteExchange;
import com.bicycle.client.kite.api.model.KiteSymbol;
import com.bicycle.core.symbol.Exchange;
import com.bicycle.core.symbol.Symbol;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KiteSymbolMapper {
    
    private final KiteExchangeMapper kiteExchangeMapper;
    private final Map<Integer, Symbol> intsrumentTokenSymbolCache = new HashMap<>();
    private final Map<KiteExchange, Map<String, Symbol>> symbolCache = new HashMap<>();
    private final Map<Exchange, Map<String, KiteSymbol>> kiteSymbolCache = new HashMap<>();
    
    public Symbol toSymbol(KiteSymbol kiteSymbol) {
        final Symbol symbol = Symbol.builder()
                .exchange(kiteExchangeMapper.toExchange(kiteSymbol.getExchange()))
                .token(kiteSymbol.getExchangeToken())
                .code(kiteSymbol.getTradingsymbol())
                .segment(kiteSymbol.getSegment())
                .tickSize(kiteSymbol.getTickSize())
                .lotSize(kiteSymbol.getLotSize())
                .name(kiteSymbol.getName())
                .type(kiteSymbol.getType())
                .build();
        cache(kiteSymbol.getInstrumentToken(), symbol);
        cache(symbol.exchange(), symbol.code(), kiteSymbol);
        cache(kiteSymbol.getExchange(), kiteSymbol.getTradingsymbol(), symbol);
        return symbol;
    }
    
    private void cache(int instrumentToken, Symbol symbol) {
        intsrumentTokenSymbolCache.put(instrumentToken, symbol);
    }
    
    public Symbol getSymbol(int instrumentToken) {
        return intsrumentTokenSymbolCache.get(instrumentToken);
    }
    
    private void cache(KiteExchange kiteExchange, String tradingsymbol, Symbol symbol) {
        symbolCache.computeIfAbsent(kiteExchange, key -> new HashMap<>()).put(tradingsymbol, symbol);
    }
    
    public Symbol getSymbol(KiteExchange kiteExchange, String tradingsymbol) {
        return symbolCache.getOrDefault(kiteExchange, Collections.emptyMap()).get(tradingsymbol);
    }
    
    private void cache(Exchange exchange, String code, KiteSymbol kiteSymbol) {
        kiteSymbolCache.computeIfAbsent(exchange, key -> new HashMap<>()).put(code, kiteSymbol);
    }

    public KiteSymbol getKiteSymbol(Exchange exchange, String code) {
        return kiteSymbolCache.getOrDefault(exchange, Collections.emptyMap()).get(code);
    }
    
    public KiteSymbol getKiteSymbol(Symbol symbol) {
        return getKiteSymbol(symbol.exchange(), symbol.code());
    }
}
