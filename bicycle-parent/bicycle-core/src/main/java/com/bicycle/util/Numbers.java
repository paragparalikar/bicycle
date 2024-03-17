package com.bicycle.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Numbers {
    
    public float truncate(float value, int numDecimalPlaces) {
        final double constant = Math.pow(10, numDecimalPlaces);
        final int intValue = (int) (value * constant);
        return ((float) intValue) / ((float)constant);
    }

}
