package com.bicycle.core.bar.provider.query.time;

import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjuster;
import java.util.function.Predicate;

public interface BarQueryTimeAdjuster extends Predicate<ZonedDateTime>, TemporalAdjuster {

}
