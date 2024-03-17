package com.bicycle.client.shoonya.api;

import com.bicycle.client.shoonya.api.model.ShoonyaBar;
import com.bicycle.client.shoonya.api.model.ShoonyaHolding;
import com.bicycle.client.shoonya.api.model.ShoonyaOrder;
import com.bicycle.client.shoonya.api.model.ShoonyaPosition;
import com.bicycle.client.shoonya.api.model.ShoonyaSymbol;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface ShoonyaApi extends AutoCloseable {
    
    void init();
    
    List<ShoonyaBar> getBars(ShoonyaSymbol symbol, int interval, Date startDate, Date endDate);

    String create(ShoonyaOrder order);

    void update(ShoonyaOrder order);

    void cancel(String orderId);

    List<ShoonyaOrder> getOrders();

    List<ShoonyaPosition> getPositions();

    List<ShoonyaHolding> getHoldings(String product);

    float getMargin(String segment);
    
    void subscribe(Collection<ShoonyaSymbol> symbols);
    
}