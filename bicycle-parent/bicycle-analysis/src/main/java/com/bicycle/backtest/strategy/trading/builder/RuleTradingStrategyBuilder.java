package com.bicycle.backtest.strategy.trading.builder;

import com.bicycle.backtest.report.cache.ReportCache;
import com.bicycle.backtest.strategy.positionSizing.PositionSizingStrategy;
import com.bicycle.backtest.strategy.trading.MockTradingStrategy;
import com.bicycle.core.indicator.IndicatorCache;
import com.bicycle.core.order.OrderType;
import com.bicycle.core.rule.Rule;
import com.bicycle.core.rule.builder.RuleBuilder;
import com.bicycle.util.ResetableIterator;
import com.bicycle.util.Strings;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RuleTradingStrategyBuilder implements TradingStrategyBuilder {
    
    private String text;
    private final OrderType entryOrderType;
    private final List<ResetableIterator> iterators;
    private RuleBuilder entryRuleBuilder, exitRuleBuilder;
    
    public RuleTradingStrategyBuilder(
            RuleBuilder entryRuleBuilder, RuleBuilder exitRuleBuilder, 
            OrderType entryOrderType, List<ResetableIterator> iterators) {
        this.entryOrderType = entryOrderType;
        this.entryRuleBuilder = entryRuleBuilder;
        this.exitRuleBuilder = exitRuleBuilder;
        this.iterators = iterators;
    }
    
    private boolean advance(int index) {
        final ResetableIterator iterator = iterators.get(index);
        if(iterator.hasNext()) {
            iterator.advance();
            return true;
        } else if(index < iterators.size() - 1) {
            if(advance(index + 1)) {
                iterator.reset();
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public List<MockTradingStrategy> build(float slippagePercentage, IndicatorCache indicatorCache, 
            ReportCache reportCache, PositionSizingStrategy positionSizingStrategy) {
        final List<MockTradingStrategy> tradingStrategies = new ArrayList<>();
        iterators.forEach(ResetableIterator::reset);
        do {
            final Rule entryRule = entryRuleBuilder.build();
            final Rule exitRule = exitRuleBuilder.build();
            tradingStrategies.add(new MockTradingStrategy(slippagePercentage, entryRule, exitRule, 
                    entryOrderType, reportCache, indicatorCache.atr(14), positionSizingStrategy));
        }while(advance(0));
        
        return tradingStrategies;
    }
    
    @Override
    public MockTradingStrategy buildDefault(float slippagePercentage, IndicatorCache indicatorCache, ReportCache reportCache, PositionSizingStrategy positionSizingStrategy) {
        return new MockTradingStrategy(slippagePercentage, entryRuleBuilder.buildDefault(), exitRuleBuilder.buildDefault(), 
                entryOrderType, reportCache, indicatorCache.atr(14), positionSizingStrategy);
    }
    
    public RuleTradingStrategyBuilder addEntryRuleBuilder(RuleBuilder ruleBuilder) {
        entryRuleBuilder = entryRuleBuilder.and(ruleBuilder);
        return this;
    }
    
    public RuleTradingStrategyBuilder addExitRuleBuilder(RuleBuilder ruleBuilder) {
        exitRuleBuilder = exitRuleBuilder.or(ruleBuilder);
        return this;
    }
    
    @Override
    public String toString() {
        return null == text ? text = new StringBuilder()
                .append(iterators.stream()
                        .filter(iterator -> Strings.hasText(iterator.name()))
                        .map(Object::toString)
                        .collect(Collectors.joining(" \n"))).append(" \n")
                .append(entryOrderType.name().toLowerCase() + " ").append(entryRuleBuilder).append(" \n")
                .append(entryOrderType.complement().name().toLowerCase() + " ").append(exitRuleBuilder).append(" \n")
                .toString() : text;
    }
    
}
