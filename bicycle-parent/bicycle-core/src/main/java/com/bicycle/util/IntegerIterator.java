package com.bicycle.util;

import lombok.Setter;

public class IntegerIterator implements ResetableIterator {

    private volatile int value;
    @Setter private String name;
    private final int defaultValue, min, max, step;
    
    public IntegerIterator(String name, int defaultValue, int min, int max, int step) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = this.min = min;
        this.max = max;
        this.step = step;
    }
    
    public boolean hasNext() {
        return value + step <= max;
    }
    
    public void advance() {
        if(!hasNext()) throw new ArrayIndexOutOfBoundsException();
        value += step;
    }
    
    public void reset() {
        value = min;
    }
    
    public int value() {
        return value;
    }
    
    public String name() {
        return name;
    }
    
    public int defaultValue() {
        return defaultValue;
    }
    
    @Override
    public boolean isSingleton() {
        return min == max;
    }
    
    @Override
    public String toString() {
        final String value = "optimize(" + defaultValue + "," + min + "," + max + "," + step + ")";
        if(Strings.hasText(name)) {
            return name + "=" + value;
        } else {
            return value;
        }
    }
    
    @Override
    public String toValueString() {
        if(isSingleton()) {
            return String.valueOf(min);
        } else if(Strings.hasText(name)) {
            return name;
        } else {
            return "optimize(" + defaultValue + "," + min + "," + max + "," + step + ")";
        }
    }
    
}
