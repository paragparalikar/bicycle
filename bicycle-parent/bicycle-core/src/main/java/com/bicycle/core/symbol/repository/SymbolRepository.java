package com.bicycle.core.symbol.repository;

import com.bicycle.core.symbol.Exchange;
import com.bicycle.core.symbol.Symbol;
import java.util.Collection;

public interface SymbolRepository {
    
    Symbol findByToken(int token, Exchange exchange);
    
    Symbol findByCode(String code, Exchange exchange);
    
    Collection<Symbol> findByExchange(Exchange exchange);
    
}
