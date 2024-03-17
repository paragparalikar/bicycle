package com.bicycle.client.yahoo.adapter.mapper;

import lombok.experimental.Delegate;

public final class YahooMapper {

    public static final YahooMapper INSTANCE = new YahooMapper();
    
    private YahooMapper() {}
    
    @Delegate private final YahooBarMapper yahooBarMapper = new YahooBarMapper();
    @Delegate private final YahooExchangeMapper yahooExchangeMapper = new YahooExchangeMapper();
    @Delegate private final YahooTimeframeMapper yahooTimeframeMapper = new YahooTimeframeMapper();
    @Delegate private final YahooSymbolMapper yahooSymbolMapper = new YahooSymbolMapper(yahooExchangeMapper);
    
}
