package com.bicycle.core.symbol.provider;

import com.bicycle.core.symbol.Exchange;
import com.bicycle.core.symbol.Symbol;
import java.util.Collection;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FilteredSymbolDataProvider implements SymbolDataProvider {
    
    private final SymbolDataProvider delegate;
    private final Predicate<Symbol> filter;

    @Override
    public Collection<Symbol> findByExchange(Exchange exchange) {
        return delegate.findByExchange(exchange).stream()
                .filter(filter).toList();
    }

}
