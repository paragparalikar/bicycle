package com.bicycle.core.indicator;

public class ATRIndicator extends MMAIndicator {

    private final int barCount;
    
    public ATRIndicator(int symbolCount, int timeframeCount, int barCount, IndicatorCache indicatorCache) {
        super(symbolCount, timeframeCount, indicatorCache.trueRange(), barCount);
        this.barCount = barCount;
    }
    
    @Override
    public String toString() {
        return toText(barCount);
    }
    
    public static String toText(int barCount) {
        return "atr(" + barCount + ")";
    }

}
