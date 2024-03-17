package com.bicycle.core.bar.dataSource;

import com.bicycle.core.bar.Bar;
import com.bicycle.core.bar.BarReader;
import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.symbol.Exchange;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

public interface BarDataSource {
    
    ZonedDateTime getEndDate(Exchange exchange, Timeframe timeframe);
    
    ZonedDateTime getStartDate(Exchange exchange, Timeframe timeframe);

    BarReader get(Exchange exchange, Timeframe timeframe);
    
    BarReader get(Exchange exchange, Timeframe timeframe, ZonedDateTime fromInclusive, ZonedDateTime toInclusive);
    
    void persist(Exchange exchange, Timeframe timeframe, Map<Long, List<Bar>> data);
    
}
