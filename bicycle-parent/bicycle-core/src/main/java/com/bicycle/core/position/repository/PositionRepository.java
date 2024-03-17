package com.bicycle.core.position.repository;

import com.bicycle.core.position.Position;
import java.util.Collection;

public interface PositionRepository {
    
    void init();

    Position save(Position position);
    
    Collection<Position> findByPortfolioId(String portfolioId);
    
}
