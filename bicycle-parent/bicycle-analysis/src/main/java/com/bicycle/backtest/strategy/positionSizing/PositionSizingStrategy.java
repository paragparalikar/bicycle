package com.bicycle.backtest.strategy.positionSizing;

import com.bicycle.core.position.Position;

public interface PositionSizingStrategy {

    int size(double ltp, double initialMargin, double availableMargin, Position position);
    
}
