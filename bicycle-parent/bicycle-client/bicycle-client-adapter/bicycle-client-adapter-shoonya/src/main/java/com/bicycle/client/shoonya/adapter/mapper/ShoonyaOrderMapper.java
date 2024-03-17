package com.bicycle.client.shoonya.adapter.mapper;

import com.bicycle.client.shoonya.api.model.ShoonyaOrder;
import com.bicycle.client.shoonya.api.model.ShoonyaSymbol;
import com.bicycle.core.order.Order;
import com.bicycle.core.symbol.Symbol;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
public class ShoonyaOrderMapper {

    private final ShoonyaSymbolMapper shoonyaSymbolMapper;
    private final ShoonyaProductMapper shoonyaProductMapper;
    private final ShoonyaExchangeMapper shoonyaExchangeMapper;
    private final ShoonyaValidityMapper shoonyaValidityMapper;
    private final ShoonyaOrderTypeMapper shoonyaOrderTypeMapper;
    private final ShoonyaLimitTypeMapper shoonyaLimitTypeMapper;
    private final ShoonyaOrderStatusMapper shoonyaOrderStatusMapper;
    private final ShoonyaAfterMarketMapper shoonyaAfterMarketMapper;
    
    public ShoonyaOrder toShoonyaOrder(Order order) {
        if(null == order) return null;
        final Symbol symbol = order.getSymbol();
        final ShoonyaSymbol shoonyaSymbol = shoonyaSymbolMapper.toShoonyaSymbol(
                symbol.exchange(), symbol.name());
        return ShoonyaOrder.builder()
                .orderId(order.getId())
                .remarks(order.getTradingStrategyId())
                .price(order.getPrice())
                .quantity(order.getQuantity())
                .trailPrice(order.getTrailPrice())
                .symbolName(shoonyaSymbol.getName())
                .triggerPrice(order.getTriggerPrice())
                .bookLossPrice(order.getBookLossPrice())
                .bookProfiltPrice(order.getBookProfiltPrice())
                .disclosedQuantity(order.getDisclosedQuantity())
                .product(shoonyaProductMapper.toShoonyaProduct(order.getProduct()))
                .orderType(shoonyaOrderTypeMapper.toShoonyaOrderType(order.getType()))
                .validity(shoonyaValidityMapper.toShoonyaValidity(order.getValidity()))
                .limitType(shoonyaLimitTypeMapper.toShoonyaLimitType(order.getLimiType()))
                .afterMarket(shoonyaAfterMarketMapper.toShoonyaAfterMarket(order.getVariety()))
                .exchange(shoonyaExchangeMapper.toShoonyaExchange(order.getSymbol().exchange()))
                .build();
    }
    
    public Order toOrder(ShoonyaOrder order) {
        if(null == order) return null;
        return Order.builder()
                .id(order.getOrderId())
                .portfolioId(order.getUserId())
                .tradingStrategyId(order.getRemarks())
                .exchangeOrderId(order.getExchangeId())
                .price(order.getPrice())
                .quantity(order.getQuantity())
                .trailPrice(order.getTrailPrice())
                .message(order.getRejectionReason())
                .averagePrice(order.getAveragePrice())
                .triggerPrice(order.getTriggerPrice())
                .bookLossPrice(order.getBookLossPrice())
                .filledQuantity(order.getFilledQuantity())
                .bookProfiltPrice(order.getBookProfiltPrice())
                .cancelledQuantity(order.getCancelledQuantity())
                .disclosedQuantity(order.getDisclosedQuantity())
                .product(shoonyaProductMapper.toProduct(order.getProduct()))
                .type(shoonyaOrderTypeMapper.toOrderType(order.getOrderType()))
                .validity(shoonyaValidityMapper.toValidity(order.getValidity()))
                .limiType(shoonyaLimitTypeMapper.toLimitType(order.getLimitType()))
                .variety(shoonyaAfterMarketMapper.toVariety(order.getAfterMarket()))
                .status(shoonyaOrderStatusMapper.toOrderStatus(order.getOrderStatus()))
                .symbol(shoonyaSymbolMapper.toSymbol(order.getExchange(), order.getSymbolName()))
                .build();
    }
    
}
