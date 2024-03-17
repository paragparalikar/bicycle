package com.bicycle.client.kite.adapter.mapper;

import lombok.experimental.Delegate;

public final class KiteMapper {
    
    public static final KiteMapper INSTANCE = new KiteMapper();
    
    private KiteMapper() {}

    @Delegate private final KiteBarMapper kiteBarMapper = new KiteBarMapper();
    @Delegate private final KiteProductMapper kiteProductMapper = new KiteProductMapper();
    @Delegate private final KiteExchangeMapper kiteExchangeMapper = new KiteExchangeMapper();
    @Delegate private final KiteIntervalMapper kiteIntervalMapper = new KiteIntervalMapper();
    @Delegate private final KiteLimitTypeMapper kiteLimitTypeMapper = new KiteLimitTypeMapper();
    @Delegate private final KiteOrderStatusMapper kiteOrderStatusMapper = new KiteOrderStatusMapper();
    @Delegate private final KiteOrderVarietyMapper kiteOrderVarietyMapper = new KiteOrderVarietyMapper();
    @Delegate private final KiteSymbolMapper kiteSymbolMapper = new KiteSymbolMapper(kiteExchangeMapper);
    @Delegate private final KiteHoldingMapper kiteHoldingMapper = new KiteHoldingMapper(kiteSymbolMapper);
    @Delegate private final KiteOrderValidityMapper kiteOrderValidityMapper = new KiteOrderValidityMapper();
    @Delegate private final KiteTransactionTypeMapper kiteTransactionTypeMapper = new KiteTransactionTypeMapper();
    @Delegate private final KitePositionMapper kitePositionMapper = new KitePositionMapper(kiteSymbolMapper, kiteProductMapper);
    @Delegate private final KiteTickMapper kiteTickMapper = new KiteTickMapper(kiteSymbolMapper);
    @Delegate private final KiteOrderMapper kiteOrderMapper = KiteOrderMapper.builder()
            .kiteExchangeMapper(kiteExchangeMapper)
            .kiteLimitTypeMapper(kiteLimitTypeMapper)
            .kiteOrderStatusMapper(kiteOrderStatusMapper)
            .kiteOrderValidityMapper(kiteOrderValidityMapper)
            .kiteOrderVarietyMapper(kiteOrderVarietyMapper)
            .kiteProductMapper(kiteProductMapper)
            .kiteSymbolMapper(kiteSymbolMapper)
            .kiteTransactionTypeMapper(kiteTransactionTypeMapper)
            .build();
}
