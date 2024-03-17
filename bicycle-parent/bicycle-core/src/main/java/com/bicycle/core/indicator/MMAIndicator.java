package com.bicycle.core.indicator;

public class MMAIndicator extends AbstractEMAIndicator {
    
    private final int barCount;
    private final Indicator indicator;

    public MMAIndicator(int symbolCount, int timeframeCount, Indicator indicator, int barCount) {
        super(symbolCount, timeframeCount, indicator, ConstantIndicator.of(1.0f / barCount));
        this.indicator = indicator;
        this.barCount = barCount;
    }
    
    @Override
    public float distance(Indicator other) {
        return Math.abs(barCount - MMAIndicator.class.cast(other).barCount);
    }
    
    @Override
    public String toString() {
        return toText(indicator, barCount);
    }
    
    public static String toText(Indicator indicator, int barCount) {
        return "mma(" + indicator + ", " + barCount + ")";
    }
    
}
