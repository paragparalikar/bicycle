package com.bicycle.util;

import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SafeConsumer<T> implements Consumer<T> {
    
    public static <T> Consumer<T> of(Consumer<T> delegate){
        return new SafeConsumer<>(delegate);
    }

    private final Consumer<T> delegate;
    
    @Override
    public void accept(T t) {
        try {
            delegate.accept(t);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
