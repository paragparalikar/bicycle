package com.bicycle.client.kite.adapter.mapper;

import com.bicycle.client.kite.api.model.KitePosition;
import com.bicycle.core.order.OrderType;
import com.bicycle.core.order.Product;
import com.bicycle.core.position.LivePosition;
import com.bicycle.core.position.Position;
import com.bicycle.core.symbol.Symbol;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KitePositionMapper {

    private final KiteSymbolMapper kiteSymbolMapper;
    private final KiteProductMapper kiteProductMapper;
    
    public Position toPosition(String portfolioId, KitePosition kitePosition) {
        if(null == kitePosition) return null;
        final Symbol symbol = kiteSymbolMapper.getSymbol(kitePosition.getExchange(), kitePosition.getTradingsymbol());
        final Product product = kiteProductMapper.toProduct(kitePosition.getProduct());
        final int currentQuantity = kitePosition.getQuantity();
        final LivePosition position = new LivePosition(symbol, null, 0 <= currentQuantity ? OrderType.BUY : OrderType.SELL);
        position.setPortfolioId(portfolioId);
        position.setCurrentQuantity(currentQuantity);
        position.setProduct(product);
        return position;
    }
    
}
