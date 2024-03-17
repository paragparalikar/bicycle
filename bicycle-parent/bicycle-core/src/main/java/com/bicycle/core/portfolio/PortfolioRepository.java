package com.bicycle.core.portfolio;

import java.util.Collection;

public interface PortfolioRepository {

    Collection<Portfolio> findAll();
    
}
