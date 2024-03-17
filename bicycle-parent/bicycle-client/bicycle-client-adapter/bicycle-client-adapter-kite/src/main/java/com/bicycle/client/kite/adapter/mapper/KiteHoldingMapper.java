package com.bicycle.client.kite.adapter.mapper;

import com.bicycle.client.kite.api.model.KiteHolding;
import com.bicycle.core.position.Holding;
import com.bicycle.core.symbol.Symbol;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KiteHoldingMapper {
    
    private final KiteSymbolMapper kiteSymbolMapper;

    public Holding toHolding(String portfolioId, KiteHolding holding) {
        if(null == holding) return null;
        final Symbol symbol = kiteSymbolMapper.getSymbol(holding.getExchange(), 
                holding.getTradingsymbol());
        return Holding.builder()
                .symbol(symbol)
                .portfolioId(portfolioId)
                .quantity(holding.getQuantity())
                .build();
    }
    
}
