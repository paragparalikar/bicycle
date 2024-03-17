package com.bicycle.backtest.strategy.positionSizing;

import com.bicycle.core.position.Position;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PercentageInitialMarginPositionSizingStrategy implements PositionSizingStrategy {
    
    private final float percentage;
    private final boolean limitToAvailableMargin;

    @Override
    public int size(double ltp, double initialMargin, double availableMargin, Position position) {
        final double maxMargin = initialMargin * percentage / 100;
        final double effectiveMargin = limitToAvailableMargin ? Math.min(maxMargin, availableMargin) : maxMargin;
        return (int)Math.floor(effectiveMargin / ltp);
    }

}
