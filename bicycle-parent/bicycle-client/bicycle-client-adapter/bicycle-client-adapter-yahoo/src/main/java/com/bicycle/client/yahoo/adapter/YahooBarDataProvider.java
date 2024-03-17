package com.bicycle.client.yahoo.adapter;

import com.bicycle.client.yahoo.adapter.mapper.YahooMapper;
import com.bicycle.client.yahoo.api.YahooHttpApi;
import com.bicycle.client.yahoo.model.YahooBar;
import com.bicycle.core.bar.Bar;
import com.bicycle.core.bar.provider.BarDataProvider;
import com.bicycle.core.bar.provider.query.BarQuery;
import java.util.List;

public class YahooBarDataProvider extends YahooHttpApi implements BarDataProvider {

    @Override
    public List<Bar> get(BarQuery barQuery) {
        final YahooMapper mapper = YahooMapper.INSTANCE;
        final List<YahooBar> yahooBars = getBars(
                mapper.toYahooSymbol(barQuery.symbol()), 
                mapper.toYahooTimeframe(barQuery.timeframe()), 
                barQuery.from(), barQuery.to());
        return yahooBars.stream().map(yahooBar -> mapper
                .toBar(barQuery.symbol(), barQuery.timeframe(), yahooBar))
                .toList();
    }

}
