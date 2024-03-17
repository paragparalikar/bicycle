package com.bicycle.core.order;

public enum OrderType {

    BUY{
        
        @Override
        public int multiplier() {
            return 1;
        }
        
        @Override
        public OrderType complement() {
            return SELL;
        }
        
    }, SELL{
        
        @Override
        public int multiplier() {
            return -1;
        }
        
        @Override
        public OrderType complement() {
            return BUY;
        }
        
    };
    
    public abstract int multiplier();
    
    public abstract OrderType complement();
    
}
