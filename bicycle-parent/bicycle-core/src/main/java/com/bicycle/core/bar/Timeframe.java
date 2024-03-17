package com.bicycle.core.bar;

import com.bicycle.Constant;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Timeframe {

    M1(1, "Minutely"), 
    M3(3, "3 Minutes"), 
    M5(5, "5 Minutes"), 
    M10(10, "10 Minutes"), 
    M15(15, "15 Minutes"), 
    M30(30, "30 Minutes"), 
    H1(60, "Hourly"), 
    H2(120, "2 Hours"), 
    H3(180, "3 Hours"), 
    D(1440, "Daily");
    
    public static Timeframe findByDisplayText(String displayText) {
        if(null == displayText || 0 == displayText.trim().length()) return null;
        for(Timeframe timeframe : Timeframe.values()) {
            if(timeframe.getDisplayText().equalsIgnoreCase(displayText))
                return timeframe;
        }
        return null;
    }
    
    @Getter private final int minuteMultiple;
    @Getter private final String displayText;
    
    public long ceil(long date) {
        final long span = minuteMultiple * 60000;
        return date + span - (date - Constant.NSE_START_EPOCH) % span;
    }
    
    public long floor(long date) {
        final long span = minuteMultiple * 60000;
        return date - (date - Constant.NSE_START_EPOCH) % span;
    }
    
    @Override
    public String toString() {
        return displayText;
    }
    
}
