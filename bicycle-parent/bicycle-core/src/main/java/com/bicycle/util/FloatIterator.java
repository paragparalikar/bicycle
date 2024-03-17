package com.bicycle.util;

import lombok.Setter;

public class FloatIterator implements ResetableIterator {

    private volatile float value;
    @Setter private String name;
    private final float defaultValue, min, max, step;
    
    public FloatIterator(String name, float defaultValue, float min, float max, float step) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = this.min = min;
        this.max = max;
        this.step = step;
    }
    
    public IntegerIterator intValue() {
        return new IntegerIterator(name, (int) defaultValue, (int) min, (int) max, (int) step);
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
    
    public float value() {
        return value;
    }
    
    public String name() {
        return name;
    }
    
    public float defaultValue() {
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
