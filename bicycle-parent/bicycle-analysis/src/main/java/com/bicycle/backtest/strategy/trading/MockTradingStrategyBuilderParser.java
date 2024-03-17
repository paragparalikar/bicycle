package com.bicycle.backtest.strategy.trading;

import com.bicycle.backtest.strategy.trading.builder.RuleTradingStrategyBuilder;
import com.bicycle.backtest.strategy.trading.builder.TradingStrategyBuilder;
import com.bicycle.core.indicator.IndicatorCache;
import com.bicycle.core.strategy.builder.TradingStrategyBuilderParser;
import java.util.List;
import lombok.Getter;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import grammer.StrategyBuilderLexer;
import grammer.StrategyBuilderParser;
import grammer.StrategyBuilderParser.StrategyBuilderContext;

public class MockTradingStrategyBuilderParser extends TradingStrategyBuilderParser {
    
    @Getter private TradingStrategyBuilder tradingStrategyBuilder;

    public MockTradingStrategyBuilderParser(IndicatorCache indicatorCache) {
        super(indicatorCache);
    }
    
    @Override
    public void exitStrategyBuilder(StrategyBuilderContext ctx) {
        tradingStrategyBuilder = new RuleTradingStrategyBuilder(
                getEntryRuleBuilder(), 
                getExitRuleBuilder(), 
                getEntryOrderType(), 
                getIterators());
    }

    public static void main(String[] args) {
        final String text = """
                smaItr = optimize(50,10,100,10) 
                rsiItr = optimize(14,3,21,3) 
                constItr = optimize(30.0,5.0,45.0,5.0) 
                buy close > sma(close, smaItr) and rsi(close, rsiItr) < constant(constItr)
                sell close < sma(close, smaItr) or rsi(close, rsiItr) > constant(constItr)
                """;
        final IndicatorCache indicatorCache = new IndicatorCache(1, 1);
        final StrategyBuilderLexer lexer = new StrategyBuilderLexer(CharStreams.fromString(text));
        final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        final StrategyBuilderParser parser = new StrategyBuilderParser(tokenStream);
        final ParseTree parseTree = parser.strategyBuilder();
        final ParseTreeWalker walker = new ParseTreeWalker();
        final MockTradingStrategyBuilderParser listener = new MockTradingStrategyBuilderParser(indicatorCache);
        walker.walk(listener, parseTree);
        
        final TradingStrategyBuilder builder = listener.getTradingStrategyBuilder();
        System.out.println(builder);
        final List<MockTradingStrategy> tradingStrategies = builder.build(0, indicatorCache, null, null);
        System.out.println("Created " + tradingStrategies.size() + " trading strategies");
        for(int index = 0; index < tradingStrategies.size(); index++) {
            final MockTradingStrategy strategy = tradingStrategies.get(index);
            System.out.println(index + " - " + strategy);
        }
    }

}
