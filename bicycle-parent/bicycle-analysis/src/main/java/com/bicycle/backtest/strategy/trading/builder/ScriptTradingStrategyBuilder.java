package com.bicycle.backtest.strategy.trading.builder;

import com.bicycle.backtest.strategy.trading.MockTradingStrategyBuilderParser;
import com.bicycle.core.indicator.IndicatorCache;
import lombok.experimental.Delegate;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import grammer.StrategyBuilderLexer;
import grammer.StrategyBuilderParser;

public class ScriptTradingStrategyBuilder implements TradingStrategyBuilder {
    
    @Delegate private final TradingStrategyBuilder delegate;
    
    public ScriptTradingStrategyBuilder(String script, IndicatorCache indicatorCache) {
        final StrategyBuilderLexer lexer = new StrategyBuilderLexer(CharStreams.fromString(script));
        final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        final StrategyBuilderParser parser = new StrategyBuilderParser(tokenStream);
        final ParseTree parseTree = parser.strategyBuilder();
        final ParseTreeWalker walker = new ParseTreeWalker();
        final MockTradingStrategyBuilderParser listener = new MockTradingStrategyBuilderParser(indicatorCache);
        walker.walk(listener, parseTree);
        delegate = listener.getTradingStrategyBuilder();
    }
    
}
