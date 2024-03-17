package com.bicycle.client.kite.api;

import com.bicycle.client.kite.api.model.KiteCandle;
import com.bicycle.client.kite.api.model.KiteHolding;
import com.bicycle.client.kite.api.model.KiteInterval;
import com.bicycle.client.kite.api.model.KiteMargin;
import com.bicycle.client.kite.api.model.KiteOrder;
import com.bicycle.client.kite.api.model.KiteOrderId;
import com.bicycle.client.kite.api.model.KitePosition;
import com.bicycle.client.kite.api.model.KiteProfile;
import com.bicycle.client.kite.api.model.KiteQuote;
import com.bicycle.client.kite.api.model.KiteQuoteMode;
import com.bicycle.client.kite.api.model.KiteSymbol;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;

public interface KiteApi extends AutoCloseable {
    
    void init();

    List<KiteCandle> getData(
	        KiteSymbol symbol, 
	        KiteInterval interval, 
	        ZonedDateTime from, 
	        ZonedDateTime to);

	KiteProfile getProfile();

	KiteMargin getMargin();

	List<KiteHolding> getHoldings();

	List<KitePosition> getPositions();

	List<KiteOrder> getOrders();

	KiteOrderId create(KiteOrder order);

	KiteOrderId update(KiteOrder order);

	KiteOrderId cancel(KiteOrder order);
	
	void subscribe(Collection<KiteSymbol> symbols);

	Collection<KiteQuote> getQuotes(Collection<KiteSymbol> instruments, KiteQuoteMode mode);
	
}