package com.bicycle.core.symbol;

import java.util.function.Predicate;
import lombok.Builder;
import lombok.With;

@Builder
public record Symbol(
        Exchange exchange, 
        int token,          // Exchange token provided by both kite and shoonya
        @With String code,  // Used by Kite, NSE, Bicycle
        @With String name,  // Used by Shoonya
        String type, 
        String segment,
        float tickSize, 
        int lotSize) {
    
    public static Predicate<Symbol> equities(){
        // endsWith - ETF
        // startsWith - EBBETF, GSEC
        // name startsWith - HDFCAMC, KOTAKMAMC
        // contains - -
        return symbol -> true;
    }
    
    @Override
    public int hashCode() {
        return token;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Symbol other = (Symbol) obj;
        return exchange == other.exchange && token == other.token;
    }
    
}
