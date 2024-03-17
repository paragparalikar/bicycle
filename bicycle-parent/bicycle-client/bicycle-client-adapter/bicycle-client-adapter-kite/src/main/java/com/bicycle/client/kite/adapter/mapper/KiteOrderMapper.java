package com.bicycle.client.kite.adapter.mapper;

import com.bicycle.client.kite.api.model.KiteOrder;
import com.bicycle.client.kite.api.model.KiteSymbol;
import com.bicycle.core.order.Order;
import com.bicycle.core.symbol.Symbol;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
public class KiteOrderMapper {
    
    private final KiteSymbolMapper kiteSymbolMapper;
    private final KiteProductMapper kiteProductMapper;
    private final KiteExchangeMapper kiteExchangeMapper;
    private final KiteLimitTypeMapper kiteLimitTypeMapper;
    private final KiteOrderStatusMapper kiteOrderStatusMapper;
    private final KiteOrderVarietyMapper kiteOrderVarietyMapper;
    private final KiteOrderValidityMapper kiteOrderValidityMapper;
    private final KiteTransactionTypeMapper kiteTransactionTypeMapper;
    
    public Order toOrder(KiteOrder kiteOrder) {
        if(null == kiteOrder) return null;
        return Order.builder()
                .id(kiteOrder.getOrderId())
                .portfolioId(kiteOrder.getPlacedBy())
                .tradingStrategyId(kiteOrder.getTag())
                .exchangeOrderId(kiteOrder.getExchangeOrderId())
                .product(kiteProductMapper.toProduct(kiteOrder.getProduct()))
                .variety(kiteOrderVarietyMapper.toVariety(kiteOrder.getVariety()))
                .symbol(kiteSymbolMapper.getSymbol(kiteOrder.getExchange(), kiteOrder.getTradingsymbol()))
                .status(kiteOrderStatusMapper.toOrderStatus(kiteOrder.getStatus()))
                .limiType(kiteLimitTypeMapper.toLimitType(kiteOrder.getLimitType()))
                .validity(kiteOrderValidityMapper.toValidity(kiteOrder.getValidity()))
                .type(kiteTransactionTypeMapper.toOrderType(kiteOrder.getTransactionType()))
                .quantity(kiteOrder.getQuantity())
                .filledQuantity(kiteOrder.getFilledQuantity())
                .cancelledQuantity(kiteOrder.getCancelledQuantity())
                .disclosedQuantity(kiteOrder.getDisclosedQuantity())
                .price(kiteOrder.getPrice())
                .triggerPrice(kiteOrder.getTriggerPrice())
                .averagePrice(kiteOrder.getAveragePrice())
                .bookProfiltPrice(kiteOrder.getSquareoff())
                .bookLossPrice(kiteOrder.getStoploss())
                .trailPrice(kiteOrder.getTrailingStoploss())
                .message(kiteOrder.getStatusMessage())
                .build();
    }
    
    public KiteOrder toKiteOrder(Order order) {
        if(null == order) return null;
        final Symbol symbol = order.getSymbol();
        final KiteSymbol kiteSymbol = kiteSymbolMapper.getKiteSymbol(symbol.exchange(), symbol.code());
        return KiteOrder.builder()
                .orderId(order.getId())
                .tag(order.getTradingStrategyId())
                .exchangeOrderId(order.getExchangeOrderId())
                .product(kiteProductMapper.toKiteProduct(order.getProduct()))
                .limitType(kiteLimitTypeMapper.toKiteLimitType(order.getLimiType()))
                .variety(kiteOrderVarietyMapper.toKiteOrderVariety(order.getVariety()))
                .exchange(kiteExchangeMapper.toKiteExchange(order.getSymbol().exchange()))
                .validity(kiteOrderValidityMapper.toKiteOrderValidity(order.getValidity()))
                .transactionType(kiteTransactionTypeMapper.toKiteTransactionType(order.getType()))
                .tradingsymbol(kiteSymbol.getTradingsymbol())
                .quantity(order.getQuantity())
                .disclosedQuantity(order.getDisclosedQuantity())
                .price(order.getPrice())
                .triggerPrice(order.getTriggerPrice())
                .stoploss(order.getBookLossPrice())
                .squareoff(order.getBookProfiltPrice())
                .trailingStoploss(order.getTrailPrice())
                .build();
    }

}
