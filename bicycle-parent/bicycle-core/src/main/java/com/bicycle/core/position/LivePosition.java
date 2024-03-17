package com.bicycle.core.position;

import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.order.OrderType;
import com.bicycle.core.order.Product;
import com.bicycle.core.symbol.Symbol;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LivePosition extends Position {
    
    private String portfolioId;
    private Product product;
    private PositionStatus status;
    private int currentQuantity;

    public LivePosition(Symbol symbol, Timeframe timeframe, OrderType entryType) {
        super(symbol, timeframe, entryType);
    }

}
