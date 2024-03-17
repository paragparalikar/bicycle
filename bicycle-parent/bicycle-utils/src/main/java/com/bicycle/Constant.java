package com.bicycle;

import java.io.File;
import java.time.LocalTime;
import java.util.Date;
import java.util.Locale;

@SuppressWarnings("deprecation") 
public interface Constant {
    
    float FIXED_RATE_OF_RETURN = 6.0f;
    Locale LOCALE = new Locale("en", "IN");
    LocalTime NSE_START_TIME = LocalTime.of(9, 15);
    long NSE_START_EPOCH = new Date(70, 0, 1, 9, 15).getTime();
    LocalTime NSE_END_TIME = LocalTime.of(15, 30);
    String HOME = System.getProperty("user.home") + File.separator + ".bicycle";
    
}
