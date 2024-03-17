package com.bicycle.client.shoonya.adapter.mapper;

import com.bicycle.client.shoonya.api.model.ShoonyaPosition;
import com.bicycle.core.order.OrderType;
import com.bicycle.core.order.Product;
import com.bicycle.core.position.LivePosition;
import com.bicycle.core.position.Position;
import com.bicycle.core.symbol.Symbol;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
public class ShoonyaPositionMapper {
    
    private final ShoonyaSymbolMapper shoonyaSymbolMapper;
    private final ShoonyaProductMapper shoonyaProductMapper;
    
    public Position toPosition(String portfolioId, ShoonyaPosition kitePosition) {
        if(null == kitePosition) return null;
        final Product product = shoonyaProductMapper.toProduct(kitePosition.getProduct());
        final Symbol symbol = shoonyaSymbolMapper.toSymbol(kitePosition.getExchange(), kitePosition.getSymbolName());
        final LivePosition position = new LivePosition(symbol, null, 0 <= kitePosition.getNetQuantity() ? 
                OrderType.BUY : OrderType.SELL);
        position.setProduct(product);
        position.setPortfolioId(portfolioId);
        position.setCurrentQuantity(kitePosition.getNetQuantity());
        return position;
    }

}
