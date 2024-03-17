package com.bicycle.util;

public interface ResetableIterator {
    
    String toValueString();
    
    boolean isSingleton();
    
    boolean hasNext();
    
    void advance();
    
    void reset();
    
    String name();

}
