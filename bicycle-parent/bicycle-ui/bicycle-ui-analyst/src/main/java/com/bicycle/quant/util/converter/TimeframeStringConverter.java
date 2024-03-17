package com.bicycle.quant.util.converter;

import com.bicycle.core.bar.Timeframe;
import javafx.util.StringConverter;

public class TimeframeStringConverter extends StringConverter<Timeframe> {
    
    public static final TimeframeStringConverter INSTANCE = new TimeframeStringConverter();
    
    @Override
    public String toString(Timeframe timeframe) {
        return null == timeframe ? null : timeframe.getDisplayText();
    }

    @Override
    public Timeframe fromString(String text) {
        return Timeframe.findByDisplayText(text);
    }

}
