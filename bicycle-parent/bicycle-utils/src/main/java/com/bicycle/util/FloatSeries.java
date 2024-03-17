package com.bicycle.util;

public class FloatSeries {
    
    private final float[] values;
    private int writePointer, size;
    
    public FloatSeries(int capacity) {
        this.values = new float[capacity];
    }
    
    public float add(float value) {
        values[writePointer++ % values.length] = value;
        if(size < values.length) size++;
        return value;
    }
    
    public int size() {
        return size;
    }
    
    public int modCount() {
        return writePointer;
    }
    
    public int capacity() {
        return values.length;
    }
    
    public void clear() {
        size = 0;
        writePointer = 0;
    }
    
    public float get(int index) {
        if(0 > index || index >= size) throw new ArrayIndexOutOfBoundsException(index);
        return values[(writePointer - 1 - index) % values.length];
    }

}
