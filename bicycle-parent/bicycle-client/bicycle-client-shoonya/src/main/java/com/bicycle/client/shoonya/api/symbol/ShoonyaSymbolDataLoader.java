package com.bicycle.client.shoonya.api.symbol;

import com.bicycle.client.shoonya.api.model.ShoonyaExchange;
import com.bicycle.client.shoonya.api.model.ShoonyaSymbol;
import java.util.Collection;

public interface ShoonyaSymbolDataLoader {

    Collection<ShoonyaSymbol> loadByShoonyaExchange(ShoonyaExchange shoonyaExchange);
    
}
