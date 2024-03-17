package com.bicycle.core.rule;

import com.bicycle.core.indicator.IndicatorCache;
import lombok.Builder;
import lombok.experimental.Delegate;

public class LiquidityRule implements Rule {
    
    @Delegate private final Rule delegate;
    
    public LiquidityRule(IndicatorCache cache) {
        this(cache, 20, 40000);
    }
    
    @Builder
    public LiquidityRule(IndicatorCache cache, float minClose, int minVolume) {
        this.delegate = cache.sma(cache.close(), 10) .greaterThan(cache.constant(minClose))
        .and(cache.sma(cache.volume(), 10) .greaterThan(cache.constant(minVolume)));
    }

}
