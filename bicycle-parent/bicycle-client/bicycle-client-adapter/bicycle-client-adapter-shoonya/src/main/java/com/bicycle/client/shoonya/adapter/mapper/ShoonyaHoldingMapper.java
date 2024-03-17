package com.bicycle.client.shoonya.adapter.mapper;

import com.bicycle.client.shoonya.api.model.ShoonyaHolding;
import com.bicycle.client.shoonya.api.model.ShoonyaSymbol;
import com.bicycle.core.position.Holding;
import com.bicycle.core.symbol.Symbol;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
public class ShoonyaHoldingMapper {
    
    private final ShoonyaSymbolMapper shoonyaSymbolMapper;

    public Holding toHolding(String portfolioId, ShoonyaHolding holding) {
        if(null == holding) return null;
        final ShoonyaSymbol shoonyaSymbol = holding.getSymbols().get(0);
        final Symbol symbol = shoonyaSymbolMapper.toSymbol(shoonyaSymbol.getExchange(), 
                shoonyaSymbol.getName());
        return Holding.builder()
                .symbol(symbol)
                .portfolioId(portfolioId)
                .quantity(holding.getHoldingQuantity())
                .build();
    }
    
}
