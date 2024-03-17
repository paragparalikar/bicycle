package com.bicycle.quant.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

@RequiredArgsConstructor
public class ObjectConstant<T> implements ObservableValue<T> {

    @Getter private final T value;
        
    @Override public void addListener(InvalidationListener var1) {}
    @Override public void removeListener(InvalidationListener var1) {}
    @Override public void addListener(ChangeListener<? super T> var1) {}
    @Override public void removeListener(ChangeListener<? super T> var1) {}
    
}