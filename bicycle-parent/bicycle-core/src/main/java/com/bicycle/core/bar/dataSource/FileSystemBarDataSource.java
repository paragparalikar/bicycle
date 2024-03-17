package com.bicycle.core.bar.dataSource;

import com.bicycle.Constant;
import com.bicycle.core.bar.Bar;
import com.bicycle.core.bar.BarReader;
import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.symbol.Exchange;
import com.bicycle.core.symbol.repository.SymbolRepository;
import com.bicycle.util.Dates;
import java.io.DataOutput;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class FileSystemBarDataSource implements BarDataSource {
    private static final int BYTES = Integer.BYTES + Long.BYTES + 4 * Float.BYTES + Integer.BYTES;
    
    private final SymbolRepository symbolRepository;
    private final Map<Path, DateLocationIndex> cache = new HashMap<>();
    
    private Path getPath(Exchange exchange, Timeframe timeframe) {
        return Paths.get(Constant.HOME, "bars", exchange.name(), timeframe.name(), "ALL");
    }
    
    private DateLocationIndex getIndex(Path path) {
        return cache.computeIfAbsent(path, DateLocationIndex::new);
    }
    
    @Override
    public ZonedDateTime getEndDate(Exchange exchange, Timeframe timeframe) {
        final Path path = getPath(exchange, timeframe);
        return Dates.toZonedDateTime(getIndex(path).tail());
    }

    @Override
    public ZonedDateTime getStartDate(Exchange exchange, Timeframe timeframe) {
        final Path path = getPath(exchange, timeframe);
        return Dates.toZonedDateTime(getIndex(path).head());
    }

    @Override
    @SneakyThrows
    public BarReader get(Exchange exchange, Timeframe timeframe) {
        final Path path = getPath(exchange, timeframe);
        if(!Files.exists(path) || BYTES > Files.size(path)) return BarReader.EMPTY;
        final FileChannel fileChannel = FileChannel.open(path);
        final long fileChannelSize = fileChannel.size();
        final MappedByteBuffer buffer = fileChannel.map(MapMode.READ_ONLY, 0, fileChannelSize);
        return new BarReader() {
            @Override public void close() throws Exception { fileChannel.close(); }
            @Override @SneakyThrows public int size() { return (int) (fileChannelSize / BYTES); }
            @Override public void readInto(Bar bar) { read(bar, exchange, timeframe, buffer); }
        };
    }

    @Override
    @SneakyThrows
    public BarReader get(Exchange exchange, Timeframe timeframe, ZonedDateTime fromInclusive, ZonedDateTime toInclusive) {
        final Path path = getPath(exchange, timeframe);
        if(!Files.exists(path) || BYTES > Files.size(path)) return BarReader.EMPTY;
        
        final long to = toInclusive.toInstant().toEpochMilli();
        final long from = fromInclusive.toInstant().toEpochMilli();
        final FileChannel fileChannel = FileChannel.open(path);
        
        final DateLocationIndex index = getIndex(path);
        final int count = index.count(from, to);
        
        final long fileChannelSize = fileChannel.size();
        final long fromLocation = Math.min(index.location(from), fileChannelSize);
        final long toLocation = Math.min(index.location(to) + index.count(to) * BYTES, fileChannelSize);
        
        final MappedByteBuffer buffer = fileChannel.map(MapMode.READ_ONLY, fromLocation, toLocation - fromLocation);
        return new BarReader() {
            @Override public void close() throws Exception { fileChannel.close(); }
            @Override @SneakyThrows public int size() { return count; }
            @Override public void readInto(Bar bar) { 
                read(bar, exchange, timeframe, buffer); 
            }
        };
    }
    
    private void read(Bar bar, Exchange exchange, Timeframe timeframe, ByteBuffer buffer) {
        bar.symbol(symbolRepository.findByToken(buffer.getInt(), exchange));
        bar.timeframe(timeframe);
        bar.date(buffer.getLong());
        bar.open(buffer.getFloat());
        bar.high(buffer.getFloat());
        bar.low(buffer.getFloat());
        bar.close(buffer.getFloat());
        bar.volume(buffer.getInt());
    }
    
    @SneakyThrows
    private void write(Bar bar, DataOutput output) {
        output.writeInt(bar.symbol().token());
        output.writeLong(bar.date());
        output.writeFloat(bar.open());
        output.writeFloat(bar.high());
        output.writeFloat(bar.low());
        output.writeFloat(bar.close());
        output.writeInt(bar.volume());
    }
    
    @Override
    @SneakyThrows
    public void persist(Exchange exchange, Timeframe timeframe, Map<Long, List<Bar>> data) {
        if(data.isEmpty()) return;
        final Path path = getPath(exchange, timeframe);
        if(!Files.exists(path)) {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
        }
        try(RandomAccessFile file = new RandomAccessFile(path.toFile(), "rw")){
            file.seek(file.length());
            final DateLocationIndex index = getIndex(path);
            data.entrySet().stream().sorted(Entry.comparingByKey()).forEach(entry -> {
                if(100 < entry.getValue().size()) {
                    try {
                        index.set(entry.getKey(), file.getFilePointer(), entry.getValue().size());
                        for(Bar bar : entry.getValue()) write(bar, file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    
}
