package com.bicycle.core.position.repository;

import com.bicycle.core.position.Position;
import java.util.Collection;
import java.util.Collections;

public class NullPositionRepository implements PositionRepository {

    @Override
    public void init() {
        
    }

    @Override
    public Position save(Position position) {
        return position;
    }

    @Override
    public Collection<Position> findByPortfolioId(String portfolioId) {
        return Collections.emptySet();
    }
    
}
