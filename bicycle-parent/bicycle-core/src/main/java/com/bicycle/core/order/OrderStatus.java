package com.bicycle.core.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {

    DISPATCH_PENDING(false), 
    DISPATCHED(false), 
    OPEN(false), 
    UPDATE_PENDING(false), 
    CANCEL_PENDING(false), 
    CANCELLED(true), 
    REJECTED(true),
    COMPLETE(true);
    
    private final boolean terminal;
    
}
