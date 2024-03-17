package com.bicycle.core.position;

import com.bicycle.core.symbol.Symbol;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Holding {
    
    private int quantity;
    private Symbol symbol;
    private String portfolioId;

}
