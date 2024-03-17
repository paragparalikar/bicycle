package com.bicycle.core.strategy.builder;

import com.bicycle.core.indicator.IndicatorCache;
import com.bicycle.core.indicator.builder.ATRIndicatorBuilder;
import com.bicycle.core.indicator.builder.CCIIndicatorBuilder;
import com.bicycle.core.indicator.builder.ChopIndicatorBuilder;
import com.bicycle.core.indicator.builder.ConstantIndicatorBuilder;
import com.bicycle.core.indicator.builder.EMAIndicatorBuilder;
import com.bicycle.core.indicator.builder.FallingStrengthIndicatorBuilder;
import com.bicycle.core.indicator.builder.GainIndicatorBuilder;
import com.bicycle.core.indicator.builder.HighestValueIndicatorBuilder;
import com.bicycle.core.indicator.builder.IndicatorBuilder;
import com.bicycle.core.indicator.builder.LossIndicatorBuilder;
import com.bicycle.core.indicator.builder.LowestValueIndicatorBuilder;
import com.bicycle.core.indicator.builder.MMAIndicatorBuilder;
import com.bicycle.core.indicator.builder.MeanDeviationIndicatorBuilder;
import com.bicycle.core.indicator.builder.PreviousValueIndicatorBuilder;
import com.bicycle.core.indicator.builder.RSIIndicatorBuilder;
import com.bicycle.core.indicator.builder.RisingStrengthIndicatorBuilder;
import com.bicycle.core.indicator.builder.SMAIndicatorBuilder;
import com.bicycle.core.indicator.builder.SingletonIndicatorBuilder;
import com.bicycle.core.indicator.builder.StandardDeviationIndicatorBuilder;
import com.bicycle.core.indicator.builder.VarianceIndicatorBuilder;
import com.bicycle.core.order.OrderType;
import com.bicycle.core.rule.DayOfWeekRule;
import com.bicycle.core.rule.MonthOfYearRule;
import com.bicycle.core.rule.WeekOfMonthRule;
import com.bicycle.core.rule.builder.EqualsRuleBuilder;
import com.bicycle.core.rule.builder.GreaterThanRuleBuilder;
import com.bicycle.core.rule.builder.LesserThanRuleBuilder;
import com.bicycle.core.rule.builder.NotRuleBuilder;
import com.bicycle.core.rule.builder.RuleBuilder;
import com.bicycle.core.rule.builder.SingletonRuleBuilder;
import com.bicycle.core.rule.builder.StopGainRuleBuilder;
import com.bicycle.core.rule.builder.StopLossRuleBuilder;
import com.bicycle.core.rule.builder.WaitForBarCountRuleBuilder;
import com.bicycle.util.FloatIterator;
import com.bicycle.util.IntegerIterator;
import com.bicycle.util.ResetableIterator;
import java.time.DayOfWeek;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.tree.TerminalNode;
import grammer.StrategyBuilderBaseListener;
import grammer.StrategyBuilderParser.AtrBuilderContext;
import grammer.StrategyBuilderParser.CciBuilderContext;
import grammer.StrategyBuilderParser.ChopBuilderContext;
import grammer.StrategyBuilderParser.CloseBuilderContext;
import grammer.StrategyBuilderParser.CloseLocationBuilderContext;
import grammer.StrategyBuilderParser.ConstantBuilderContext;
import grammer.StrategyBuilderParser.DayOfWeekRuleBuilderContext;
import grammer.StrategyBuilderParser.EmaBuilderContext;
import grammer.StrategyBuilderParser.FallingStrengthBuilderContext;
import grammer.StrategyBuilderParser.GainBuilderContext;
import grammer.StrategyBuilderParser.HighBuilderContext;
import grammer.StrategyBuilderParser.HighestValueBuilderContext;
import grammer.StrategyBuilderParser.IndicatorBuilderContext;
import grammer.StrategyBuilderParser.IndicatorRuleBuilderContext;
import grammer.StrategyBuilderParser.IteratorContext;
import grammer.StrategyBuilderParser.LongEntryActionStatementContext;
import grammer.StrategyBuilderParser.LongExitActionStatementContext;
import grammer.StrategyBuilderParser.LossBuilderContext;
import grammer.StrategyBuilderParser.LowBuilderContext;
import grammer.StrategyBuilderParser.LowestValueBuilderContext;
import grammer.StrategyBuilderParser.MeanDevBuilderContext;
import grammer.StrategyBuilderParser.MmaBuilderContext;
import grammer.StrategyBuilderParser.MonthRuleBuilderContext;
import grammer.StrategyBuilderParser.NamedIteratorStatementContext;
import grammer.StrategyBuilderParser.NegatedRuleBuilderContext;
import grammer.StrategyBuilderParser.OpenBuilderContext;
import grammer.StrategyBuilderParser.ParameterContext;
import grammer.StrategyBuilderParser.PreviousValueBuilderContext;
import grammer.StrategyBuilderParser.RisingStrengthBuilderContext;
import grammer.StrategyBuilderParser.RsiBuilderContext;
import grammer.StrategyBuilderParser.RuleBuilderContext;
import grammer.StrategyBuilderParser.ShortEntryActionStatementContext;
import grammer.StrategyBuilderParser.SmaBuilderContext;
import grammer.StrategyBuilderParser.StdDevBuilderContext;
import grammer.StrategyBuilderParser.StopGainRuleBuilderContext;
import grammer.StrategyBuilderParser.StopLossRuleBuilderContext;
import grammer.StrategyBuilderParser.TrueRangeBuilderContext;
import grammer.StrategyBuilderParser.TypicalPriceBuilderContext;
import grammer.StrategyBuilderParser.VarianceBuilderContext;
import grammer.StrategyBuilderParser.VolumeBuilderContext;
import grammer.StrategyBuilderParser.WaitForBarCountRuleBuilderContext;
import grammer.StrategyBuilderParser.WeekOfMonthRuleBuilderContext;

@RequiredArgsConstructor
public class TradingStrategyBuilderParser  extends StrategyBuilderBaseListener {
    
    @Getter private OrderType entryOrderType;
    private final IndicatorCache indicatorCache;
    @Getter private RuleBuilder entryRuleBuilder, exitRuleBuilder;
    @Getter private final List<ResetableIterator> iterators = new ArrayList<>();
    
    
    private final Stack<RuleBuilder> ruleBuilders = new Stack<>();
    private final Stack<IndicatorBuilder> indicatorBuilders = new Stack<>();
    private final Stack<FloatIterator> floatIterators = new Stack<>();
    private final Stack<FloatIterator> parameters = new Stack<>();
    private final Map<String, FloatIterator> namedFloatIterators = new HashMap<>();
    
    @Override
    public void exitIterator(IteratorContext ctx) {
        final FloatIterator floatIterator = new FloatIterator("", 
                Float.parseFloat(ctx.NUMBER(0).getText()),
                Float.parseFloat(ctx.NUMBER(1).getText()),
                Float.parseFloat(ctx.NUMBER(2).getText()),
                Float.parseFloat(ctx.NUMBER(3).getText()));
        floatIterators.push(floatIterator);
        iterators.add(floatIterator);
    }
    
    @Override
    public void exitNamedIteratorStatement(NamedIteratorStatementContext ctx) {
        final String name = ctx.WORD().getText();
        final FloatIterator floatIterator = floatIterators.pop();
        floatIterator.setName(name);
        namedFloatIterators.put(name, floatIterator);
    }
    
    @Override
    public void exitParameter(ParameterContext ctx) {
        if(null != ctx.NUMBER()) {
            final float value = Float.parseFloat(ctx.NUMBER().getText());
            parameters.push(new FloatIterator("", value, value, value, 1));
        } else if (null != ctx.WORD()) {
            parameters.push(namedFloatIterators.get(ctx.WORD().getText()));
        } else {
            parameters.push(floatIterators.pop());
        }
    }
    
    @Override
    public void exitOpenBuilder(OpenBuilderContext ctx) {
        indicatorBuilders.push(new SingletonIndicatorBuilder(indicatorCache.open()));
    }
    
    @Override
    public void exitHighBuilder(HighBuilderContext ctx) {
        indicatorBuilders.push(new SingletonIndicatorBuilder(indicatorCache.high()));
    }
    
    @Override
    public void exitLowBuilder(LowBuilderContext ctx) {
        indicatorBuilders.push(new SingletonIndicatorBuilder(indicatorCache.low()));
    }
    
    @Override
    public void exitCloseBuilder(CloseBuilderContext ctx) {
        indicatorBuilders.push(new SingletonIndicatorBuilder(indicatorCache.close()));
    }
    
    @Override
    public void exitVolumeBuilder(VolumeBuilderContext ctx) {
        indicatorBuilders.push(new SingletonIndicatorBuilder(indicatorCache.volume()));
    }
    
    @Override
    public void exitTrueRangeBuilder(TrueRangeBuilderContext ctx) {
        indicatorBuilders.push(new SingletonIndicatorBuilder(indicatorCache.trueRange()));
    }
    
    @Override
    public void exitTypicalPriceBuilder(TypicalPriceBuilderContext ctx) {
        indicatorBuilders.push(new SingletonIndicatorBuilder(indicatorCache.typicalPrice()));
    }
    
    @Override
    public void exitCloseLocationBuilder(CloseLocationBuilderContext ctx) {
        indicatorBuilders.push(new SingletonIndicatorBuilder(indicatorCache.closeLocation()));
    }
    


    
    
    @Override
    public void exitConstantBuilder(ConstantBuilderContext ctx) {
        indicatorBuilders.push(new ConstantIndicatorBuilder(parameters.pop(), indicatorCache));
    }
    
    @Override
    public void exitChopBuilder(ChopBuilderContext ctx) {
        indicatorBuilders.push(new ChopIndicatorBuilder(parameters.pop().intValue(), indicatorCache));
    }
    
    @Override
    public void exitCciBuilder(CciBuilderContext ctx) {
        indicatorBuilders.push(new CCIIndicatorBuilder(parameters.pop().intValue(), indicatorCache));
    }
    
    @Override
    public void exitAtrBuilder(AtrBuilderContext ctx) {
        indicatorBuilders.push(new ATRIndicatorBuilder(parameters.pop().intValue(), indicatorCache));
    }
    
    @Override
    public void exitLossBuilder(LossBuilderContext ctx) {
        indicatorBuilders.push(new LossIndicatorBuilder(indicatorBuilders.pop(), indicatorCache));
    }
    
    @Override
    public void exitGainBuilder(GainBuilderContext ctx) {
        indicatorBuilders.push(new GainIndicatorBuilder(indicatorBuilders.pop(), indicatorCache));
    }
    
    private IntegerIterator intValue(FloatIterator floatIterator) {
        final IntegerIterator integerIterator = floatIterator.intValue();
        iterators.remove(floatIterator);
        iterators.add(integerIterator);
        return integerIterator;
    }
    
    @Override
    public void exitSmaBuilder(SmaBuilderContext ctx) {
        indicatorBuilders.push(new SMAIndicatorBuilder(indicatorBuilders.pop(), intValue(parameters.pop()), indicatorCache));
    }
    
    @Override
    public void exitEmaBuilder(EmaBuilderContext ctx) {
        indicatorBuilders.push(new EMAIndicatorBuilder(indicatorBuilders.pop(), intValue(parameters.pop()), indicatorCache));
    }
    
    @Override
    public void exitMmaBuilder(MmaBuilderContext ctx) {
        indicatorBuilders.push(new MMAIndicatorBuilder(indicatorBuilders.pop(), intValue(parameters.pop()), indicatorCache));
    }
    
    @Override
    public void exitRsiBuilder(RsiBuilderContext ctx) {
        indicatorBuilders.push(new RSIIndicatorBuilder(indicatorBuilders.pop(), intValue(parameters.pop()), indicatorCache));
    }
    
    @Override
    public void exitFallingStrengthBuilder(FallingStrengthBuilderContext ctx) {
        indicatorBuilders.push(new FallingStrengthIndicatorBuilder(indicatorBuilders.pop(), intValue(parameters.pop()), indicatorCache));
    }
    
    @Override
    public void exitRisingStrengthBuilder(RisingStrengthBuilderContext ctx) {
        indicatorBuilders.push(new RisingStrengthIndicatorBuilder(indicatorBuilders.pop(), intValue(parameters.pop()), indicatorCache));
    }
    
    @Override
    public void exitHighestValueBuilder(HighestValueBuilderContext ctx) {
        indicatorBuilders.push(new HighestValueIndicatorBuilder(indicatorBuilders.pop(), intValue(parameters.pop()), indicatorCache));
    }
    
    @Override
    public void exitLowestValueBuilder(LowestValueBuilderContext ctx) {
        indicatorBuilders.push(new LowestValueIndicatorBuilder(indicatorBuilders.pop(), intValue(parameters.pop()), indicatorCache));
    }
    
    @Override
    public void exitPreviousValueBuilder(PreviousValueBuilderContext ctx) {
        indicatorBuilders.push(new PreviousValueIndicatorBuilder(indicatorBuilders.pop(), intValue(parameters.pop()), indicatorCache));
    }
    
    @Override
    public void exitVarianceBuilder(VarianceBuilderContext ctx) {
        indicatorBuilders.push(new VarianceIndicatorBuilder(indicatorBuilders.pop(), intValue(parameters.pop()), indicatorCache));
    }
    
    @Override
    public void exitMeanDevBuilder(MeanDevBuilderContext ctx) {
        indicatorBuilders.push(new MeanDeviationIndicatorBuilder(indicatorBuilders.pop(), intValue(parameters.pop()), indicatorCache));
    }
    
    @Override
    public void exitStdDevBuilder(StdDevBuilderContext ctx) {
        indicatorBuilders.push(new StandardDeviationIndicatorBuilder(indicatorBuilders.pop(), intValue(parameters.pop()), indicatorCache));
    }
    
    @Override
    public void exitIndicatorBuilder(IndicatorBuilderContext ctx) {
        if(null != ctx.ARITHMATIC_OPERATOR()) {
            final IndicatorBuilder right = indicatorBuilders.pop();
            final IndicatorBuilder left = indicatorBuilders.pop();
            switch(ctx.ARITHMATIC_OPERATOR().getText()) {
                case "+" : indicatorBuilders.push(left.plus(right)); break;
                case "-" : indicatorBuilders.push(left.minus(right)); break;
                case "*" : indicatorBuilders.push(left.multipliedBy(right)); break;
                case "/" : indicatorBuilders.push(left.dividedBy(right)); break;
                case "^" : indicatorBuilders.push(left.pow(right)); break;
            }
        }
    }
    
    @Override
    public void exitDayOfWeekRuleBuilder(DayOfWeekRuleBuilderContext ctx) {
        final List<DayOfWeek> days = ctx.DAY().stream()
                .map(TerminalNode::getText)
                .map(DayOfWeek::valueOf)
                .toList();
        ruleBuilders.push(new SingletonRuleBuilder(new DayOfWeekRule(indicatorCache, days)));
    }
    
    @Override
    public void exitWeekOfMonthRuleBuilder(WeekOfMonthRuleBuilderContext ctx) {
        final List<Integer> weeks = ctx.INTEGER().stream()
                .map(TerminalNode::getText)
                .map(Integer::parseInt)
                .toList();
        ruleBuilders.push(new SingletonRuleBuilder(new WeekOfMonthRule(indicatorCache, weeks)));
    }
    
    @Override
    public void exitMonthRuleBuilder(MonthRuleBuilderContext ctx) {
        final List<Month> months = ctx.MONTH().stream()
                .map(TerminalNode::getText)
                .map(Month::valueOf)
                .toList();
        ruleBuilders.push(new SingletonRuleBuilder(new MonthOfYearRule(indicatorCache, months)));
    }
    
    @Override
    public void exitStopGainRuleBuilder(StopGainRuleBuilderContext ctx) {
        ruleBuilders.push(new StopGainRuleBuilder(parameters.pop()));
    }
    
    @Override
    public void exitStopLossRuleBuilder(StopLossRuleBuilderContext ctx) {
        ruleBuilders.push(new StopLossRuleBuilder(parameters.pop()));
    }
    
    @Override
    public void exitWaitForBarCountRuleBuilder(WaitForBarCountRuleBuilderContext ctx) {
        ruleBuilders.push(new WaitForBarCountRuleBuilder(parameters.pop().intValue(), indicatorCache));
    }
    
    @Override
    public void exitIndicatorRuleBuilder(IndicatorRuleBuilderContext ctx) {
        final IndicatorBuilder right = indicatorBuilders.pop();
        final IndicatorBuilder left = indicatorBuilders.pop();        
        switch(ctx.COMPARATOR().getText()) {
            case ">" : ruleBuilders.push(new GreaterThanRuleBuilder(left, right)); break;
            case "<" : ruleBuilders.push(new LesserThanRuleBuilder(left, right)); break;
            case "=" : ruleBuilders.push(new EqualsRuleBuilder(left, right)); break;
        }
    }
    
    @Override
    public void exitNegatedRuleBuilder(NegatedRuleBuilderContext ctx) {
        ruleBuilders.push(new NotRuleBuilder(ruleBuilders.pop()));
    }
    
    @Override
    public void exitRuleBuilder(RuleBuilderContext ctx) {
        if(null != ctx.LOGICAL_OPERATOR()) {
            final RuleBuilder right = ruleBuilders.pop();
            final RuleBuilder left = ruleBuilders.pop();
            switch(ctx.LOGICAL_OPERATOR().getText()) {
                case "and" : ruleBuilders.push(left.and(right)); break;
                case "or" : ruleBuilders.push(left.or(right)); break;
            }
        }
    }
    
    @Override
    public void exitLongEntryActionStatement(LongEntryActionStatementContext ctx) {
        entryOrderType = OrderType.BUY;
        entryRuleBuilder = ruleBuilders.pop();
    }
    
    @Override
    public void exitLongExitActionStatement(LongExitActionStatementContext ctx) {
        exitRuleBuilder = ruleBuilders.pop();
    }
    
    @Override
    public void exitShortEntryActionStatement(ShortEntryActionStatementContext ctx) {
        entryOrderType = OrderType.SELL;
        entryRuleBuilder = ruleBuilders.pop();
    }
    
}
