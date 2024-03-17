package com.bicycle.client.kite.api.symbol;

import com.bicycle.client.kite.api.model.KiteExchange;
import com.bicycle.client.kite.api.model.KiteSymbol;
import java.util.Collection;

public interface KiteSymbolDataLoader {

    Collection<KiteSymbol> loadByKiteExchange(KiteExchange kiteExchange);
    
}
