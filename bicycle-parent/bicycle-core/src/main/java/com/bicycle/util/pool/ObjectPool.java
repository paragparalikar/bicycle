package com.bicycle.util.pool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ObjectPool<T> {

    private final Supplier<T> creater;
    private final List<T> cache = new ArrayList<>();
    private final AtomicLong lock = new AtomicLong();
    
    public T aquire(){
        if(cache.isEmpty()) {
            return creater.get();
        } else {
            T object = null;
            long sequence = 0;
            do {
                sequence = lock.incrementAndGet();
                object = cache.remove(cache.size() - 1);
            } while(!cache.isEmpty() && sequence != lock.get());
            return null == object ? creater.get() : object;
        }
    }
    
    public void release(T object) {
        cache.add(object);
    }
    
    public void clear() {
        cache.clear();
    }
    
}
