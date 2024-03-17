package com.bicycle.core.portfolio;

import java.util.Collection;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CachePortfolioRepository implements PortfolioRepository {
    
    private Collection<Portfolio> portfolios;
    private final PortfolioRepository delegate;

    @Override
    public Collection<Portfolio> findAll() {
        return Optional.ofNullable(portfolios).orElseGet(this::load);
    }
    
    private Collection<Portfolio> load(){
        return portfolios = delegate.findAll();
    }

}
