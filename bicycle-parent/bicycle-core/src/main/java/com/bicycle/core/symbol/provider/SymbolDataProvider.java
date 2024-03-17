package com.bicycle.core.symbol.provider;

import com.bicycle.core.symbol.Exchange;
import com.bicycle.core.symbol.Symbol;
import java.util.Collection;

public interface SymbolDataProvider {
    
    Collection<Symbol> findByExchange(Exchange exchange);
    
}
