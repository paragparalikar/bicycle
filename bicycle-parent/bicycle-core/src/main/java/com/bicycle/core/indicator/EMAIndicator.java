package com.bicycle.core.indicator;

public class EMAIndicator extends AbstractEMAIndicator {

    private final int barCount;
    private final Indicator indicator;
    
    public EMAIndicator(int symbolCount, int timeframeCount, int barCount, Indicator indicator) {
        super(symbolCount, timeframeCount, indicator, ConstantIndicator.of((float) (2.0 / (barCount + 1))));
        this.barCount = barCount;
        this.indicator = indicator;
    }
    
    @Override
    public float distance(Indicator other) {
        return Math.abs(barCount - EMAIndicator.class.cast(other).barCount);
    }
    
    @Override
    public String toString() {
        return toText(barCount, indicator);
    }
    
    public static String toText(int barCount, Indicator indicator) {
        return "ema(" + indicator + "," + barCount + ")";
    }

}
