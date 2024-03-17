package com.bicycle.core.bar;

public interface BarReader extends AutoCloseable {
    
    public static final BarReader EMPTY = new BarReader() {
        @Override public void close() throws Exception {}
        @Override public int size() { return 0; }
        @Override public void readInto(Bar bar) {}
    };
    
    int size();

    void readInto(Bar bar);
    
}
