package com.bicycle.core.indicator;

import com.bicycle.core.bar.Bar;
import com.bicycle.core.bar.BarListener;
import com.bicycle.core.bar.BarSeries;
import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.symbol.Symbol;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

@RequiredArgsConstructor
public class IndicatorCache implements BarListener {

    private final int symbolCount, timeframeCount;
    private final Int2ObjectOpenHashMap<BarSeries> barSeriesCache;
    private final List<Indicator> indicators = new ArrayList<>();
    private final Map<String, Indicator> cache = new ConcurrentHashMap<>();
    
    public IndicatorCache(int symbolCount, int timeframeCount) {
        this.symbolCount = symbolCount;
        this.timeframeCount = timeframeCount;
        this.barSeriesCache = new Int2ObjectOpenHashMap<>(symbolCount);
    }
    
    private Indicator cache(Indicator indicator) {
        indicators.add(indicator);
        return indicator;
    }
    
    public void clear() {
        indicators.forEach(indicator -> indicator.clear());
        barSeriesCache.values().forEach(BarSeries::clear);
    }
    
    @Override
    public void onBar(Bar bar) {
        barSeries(bar.symbol(), bar.timeframe()).add(bar);
        indicators.forEach(indicator -> indicator.onBar(bar));
    }
    
    public void onTick(Symbol symbol, Timeframe timeframe, long date, float price, int volume) {
        barSeries(symbol, timeframe).onTick(date, price, volume);
    }
    
    public Indicator constant(float value) {
        return ConstantIndicator.of(value);
    }
    
    public BarSeries barSeries(Symbol symbol, Timeframe timeframe) {
        return barSeriesCache.computeIfAbsent(symbol.token(), token -> new BarSeries(symbol, timeframe, 64));
    }
    
    public Indicator open() {
        return computeIfAbsent(OpenPriceIndicator.toText(), 
                () -> cache(new OpenPriceIndicator(symbolCount, timeframeCount)));
    }
    
    public Indicator low() {
        return computeIfAbsent(LowPriceIndicator.toText(), 
                () -> cache(new LowPriceIndicator(symbolCount, timeframeCount)));
    }
    
    public Indicator high() {
        return computeIfAbsent(HighPriceIndicator.toText(), 
                () -> cache(new HighPriceIndicator(symbolCount, timeframeCount)));
    }
    
    public Indicator close() {
        return computeIfAbsent(ClosePriceIndicator.toText(), 
                () -> cache(new ClosePriceIndicator(symbolCount, timeframeCount)));
    }
    
    public Indicator volume() {
        return computeIfAbsent(VolumeIndicator.toText(), 
                () -> cache(new VolumeIndicator(symbolCount, timeframeCount)));
    }
    
    public Indicator typicalPrice() {
        return computeIfAbsent(TypicalPriceIndicator.toText(), 
                () -> cache(new TypicalPriceIndicator(symbolCount, timeframeCount)));
    }
    
    public Indicator trueRange() {
        return computeIfAbsent(TRIndicator.toText(), 
                () -> cache(new TRIndicator(symbolCount, timeframeCount, this)));
    }
    
    public Indicator closeLocation() {
        return cache.computeIfAbsent(CloseLocationIndicator.toText(), 
                k -> cache(new CloseLocationIndicator(symbolCount, timeframeCount)));
    }
    
    public Indicator atr(int barCount) {
        return computeIfAbsent(ATRIndicator.toText(barCount), 
                () -> cache(new ATRIndicator(symbolCount, timeframeCount, barCount, this)));
    }
    
    public Indicator sma(Indicator indicator, int barCount) {
        return computeIfAbsent(SMAIndicator.toText(indicator, barCount), 
                () -> cache(new SMAIndicator(symbolCount, timeframeCount, indicator, barCount)));
    }
    
    public Indicator mma(Indicator indicator, int barCount) {
        return computeIfAbsent(MMAIndicator.toText(indicator, barCount), 
                () -> cache(new MMAIndicator(symbolCount, timeframeCount, indicator, barCount)));
    }
    
    public Indicator ema(Indicator indicator, int barCount) {
        return computeIfAbsent(EMAIndicator.toText(barCount, indicator), 
                () -> cache(new EMAIndicator(symbolCount, timeframeCount, barCount, indicator)));
    }
    
    public Indicator gain(Indicator indicator) {
        return computeIfAbsent(GainIndicator.toText(indicator), 
                () -> cache(new GainIndicator(symbolCount, timeframeCount, indicator, this)));
    }
    
    public Indicator loss(Indicator indicator) {
        return computeIfAbsent(LossIndicator.toText(indicator), 
                () -> cache(new LossIndicator(symbolCount, timeframeCount, indicator, this)));
    }
    
    public Indicator rsi(Indicator indicator, int barCount) {
        return computeIfAbsent(RSIIndicator.toText(indicator, barCount),
                () -> cache(new RSIIndicator(symbolCount, timeframeCount, indicator, barCount, this)));
    }
    
    public Indicator cci(int barCount) {
        return computeIfAbsent(CCIIndicator.toText(barCount),
                () -> cache(new CCIIndicator(symbolCount, timeframeCount, barCount, this)));
    }
    
    public Indicator chop(int barCount) {
        return computeIfAbsent(ChopIndicator.toText(barCount),
                () -> cache(new ChopIndicator(symbolCount, timeframeCount, barCount, this)));
    }
    
    public Indicator variance(Indicator indicator, int barCount) {
        return computeIfAbsent(VarianceIndicator.toText(indicator, barCount), 
                () -> cache(new VarianceIndicator(symbolCount, timeframeCount, indicator, barCount, this)));
    }
    
    public Indicator stdDev(Indicator indicator, int barCount) {
        return computeIfAbsent(StandardDeviationIndicator.toText(indicator, barCount), 
                () -> cache(new StandardDeviationIndicator(symbolCount, timeframeCount, indicator, barCount, this)));
    }
    
    public Indicator meanDev(Indicator indicator, int barCount) {
        return computeIfAbsent(MeanDeviationIndicator.toText(barCount, indicator), 
                () -> cache(new MeanDeviationIndicator(symbolCount, timeframeCount, barCount, indicator, this)));
    }
    
    public Indicator prev(Indicator indicator, int barCount) {
        return computeIfAbsent(PreviousValueIndicator.toText(indicator, barCount), 
                () -> cache(new PreviousValueIndicator(symbolCount, timeframeCount, indicator, barCount)));
    }
    
    public Indicator highest(Indicator indicator, int barCount) {
        return computeIfAbsent(HighestValueIndicator.toText(indicator, barCount), 
                () -> cache(new HighestValueIndicator(symbolCount, timeframeCount, indicator, barCount)));
    }
    
    public Indicator lowest(Indicator indicator, int barCount) {
        return computeIfAbsent(LowestValueIndicator.toText(indicator, barCount), 
                () -> cache(new LowestValueIndicator(symbolCount, timeframeCount, indicator, barCount)));
    }
    
    public Indicator risingStrength(Indicator indicator, int barCount) {
        return computeIfAbsent(RisingStrengthIndicator.toText(indicator, barCount), 
                () -> cache(new RisingStrengthIndicator(symbolCount, timeframeCount, indicator, barCount)));
    }
    
    public Indicator fallingStrength(Indicator indicator, int barCount) {
        return computeIfAbsent(FallingStrengthIndicator.toText(indicator, barCount), 
                () -> cache(new FallingStrengthIndicator(symbolCount, timeframeCount, indicator, barCount)));
    }
    
    private Indicator computeIfAbsent(String text, Supplier<Indicator> supplier) {
        Indicator indicator = cache.get(text);
        if(null == indicator) {
            indicator = supplier.get();
            cache.put(text, indicator);
        }
        return indicator;
    }
    
}
