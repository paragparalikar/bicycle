package com.bicycle.core.bar.provider.query.time;

import com.bicycle.core.bar.Timeframe;
import java.time.ZonedDateTime;
import java.util.function.BiFunction;

public interface BarQueryMarketTimeAdjuster extends BiFunction<Timeframe, ZonedDateTime, ZonedDateTime> {

}
