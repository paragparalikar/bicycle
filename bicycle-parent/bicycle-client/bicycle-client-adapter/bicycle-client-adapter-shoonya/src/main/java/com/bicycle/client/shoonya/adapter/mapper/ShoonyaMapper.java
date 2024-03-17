package com.bicycle.client.shoonya.adapter.mapper;

import lombok.experimental.Delegate;

public final class ShoonyaMapper {
    
    public static final ShoonyaMapper INSTANCE = new ShoonyaMapper();
    
    private ShoonyaMapper() {}

    @Delegate private final ShoonyaBarMapper shoonyaBarMapper = new ShoonyaBarMapper();
    @Delegate private final ShoonyaProductMapper shoonyaProductMapper = new ShoonyaProductMapper();
    @Delegate private final ShoonyaSegmentMapper shoonyaSegmentMapper = new ShoonyaSegmentMapper();
    @Delegate private final ShoonyaExchangeMapper shoonyaExchangeMapper = new ShoonyaExchangeMapper();
    @Delegate private final ShoonyaSymbolMapper shoonyaSymbolMapper = new ShoonyaSymbolMapper(shoonyaExchangeMapper);
    @Delegate private final ShoonyaHoldingMapper shoonyaHoldingMapper = new ShoonyaHoldingMapper(shoonyaSymbolMapper);
    @Delegate private final ShoonyaPositionMapper shoonyaPositionMapper = new ShoonyaPositionMapper(shoonyaSymbolMapper, shoonyaProductMapper);
    @Delegate private final ShoonyaIntervalMapper shoonyaIntervalMapper = new ShoonyaIntervalMapper();
    @Delegate private final ShoonyaValidityMapper shoonyaValidityMapper = new ShoonyaValidityMapper();
    @Delegate private final ShoonyaOrderTypeMapper shoonyaOrderTypeMapper = new ShoonyaOrderTypeMapper();
    @Delegate private final ShoonyaLimitTypeMapper shoonyaLimitTypeMapper = new ShoonyaLimitTypeMapper();
    @Delegate private final ShoonyaOrderStatusMapper shoonyaOrderStatusMapper = new ShoonyaOrderStatusMapper();
    @Delegate private final ShoonyaAfterMarketMapper shoonyaAfterMarketMapper = new ShoonyaAfterMarketMapper();
    @Delegate private final ShoonyaTickMapper shoonyaTickMapper = new ShoonyaTickMapper(shoonyaSymbolMapper);
    @Delegate private final ShoonyaOrderMapper shoonyaOrderMapper = ShoonyaOrderMapper.builder()
            .shoonyaSymbolMapper(shoonyaSymbolMapper)
            .shoonyaProductMapper(shoonyaProductMapper)
            .shoonyaExchangeMapper(shoonyaExchangeMapper)
            .shoonyaValidityMapper(shoonyaValidityMapper)
            .shoonyaOrderStatusMapper(shoonyaOrderStatusMapper)
            .shoonyaLimitTypeMapper(shoonyaLimitTypeMapper)
            .shoonyaOrderTypeMapper(shoonyaOrderTypeMapper)
            .shoonyaAfterMarketMapper(shoonyaAfterMarketMapper)
            .build();
    
}
