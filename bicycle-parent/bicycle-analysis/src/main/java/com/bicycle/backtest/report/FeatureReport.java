package com.bicycle.backtest.report;

import com.bicycle.backtest.MockPosition;
import com.bicycle.backtest.indicator.IndicatorGroupManager;
import com.bicycle.util.pool.ObjectPool;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.experimental.Delegate;

public class FeatureReport implements Report, AutoCloseable {

    private final RandomAccessFile file;
    @Delegate private final Report delegate;
    private final IndicatorGroupManager indicatorGroupManager;
    private final Map<Integer, ByteBuffer> cache = new HashMap<>();
    private final ObjectPool<ByteBuffer> pool;

    public FeatureReport(Report delegate, IndicatorGroupManager indicatorGroupManager) {
        this.delegate = delegate;
        this.file = createFile();
        this.indicatorGroupManager = indicatorGroupManager;
        final int byteBufferSize = (indicatorGroupManager.count() + 1) * Float.BYTES;
        this.pool = new ObjectPool<>(() -> ByteBuffer.allocate(byteBufferSize));
    }
    
    @SneakyThrows
    private RandomAccessFile createFile() {
        final File file = Files.createTempFile("bicycle", "features").toFile();
        file.deleteOnExit();
        return new RandomAccessFile(file, "rw");
    }
    
    @Override
    public void open(MockPosition trade) {
        final ByteBuffer byteBuffer = pool.aquire();
        byteBuffer.clear();
        indicatorGroupManager.capture(trade.getSymbol(), trade.getTimeframe(), byteBuffer);
        cache.put(trade.getId(), byteBuffer);
    }

    @Override
    @SneakyThrows
    public void close(MockPosition trade) {
        final ByteBuffer byteBuffer = cache.remove(trade.getId());
        byteBuffer.putFloat(trade.getClosePercentProfitLoss());
        file.write(byteBuffer.array());
        pool.release(byteBuffer);
    }
    
    public int getFeatureCount() {
        return indicatorGroupManager.count();
    }
    
    @Override
    @SuppressWarnings("unchecked") 
    public <T extends Report> T unwrap(Class<T> type) {
        if(type.isAssignableFrom(getClass())) return (T) this;
        return delegate.unwrap(type);
    }
    
    @Override
    public void close() throws Exception {
        file.close();
        cache.clear();
        pool.clear();
    }
    
}
