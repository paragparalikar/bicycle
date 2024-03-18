package com.bicycle.core.bar.repository;

import com.bicycle.Constant;
import com.bicycle.core.bar.Bar;
import com.bicycle.core.bar.BarReader;
import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.symbol.Symbol;
import com.bicycle.core.symbol.repository.SymbolRepository;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class FileSystemBarRepository implements BarRepository {
    private static final int BYTES = Long.BYTES + 4 * Float.BYTES + Integer.BYTES;
    
    @Getter private final SymbolRepository symbolRepository;

    @Override
    public void close() throws Exception {}

    @Override
    @SneakyThrows
    public void init() {
        final Path path = Paths.get(Constant.HOME, "bars");
        if(!Files.exists(path)) Files.createDirectories(path.getParent());
    }
    
    private Path getPath(Symbol symbol, Timeframe timeframe) {
        return Paths.get(Constant.HOME, "bars", symbol.exchange().name(), timeframe.name(), symbol.code());
    }
    
    @SneakyThrows
    private Bar read(Symbol symbol, Timeframe timeframe, DataInput input) {
        return Bar.builder()
                .symbol(symbol)
                .timeframe(timeframe)
                .date(input.readLong())
                .open(input.readFloat())
                .high(input.readFloat())
                .low(input.readFloat())
                .close(input.readFloat())
                .volume(input.readInt())
                .build();
    }
    
    @SneakyThrows
    private void read(Bar bar, DataInput input) {
        bar.date(input.readLong());
        bar.open(input.readFloat());
        bar.high(input.readFloat());
        bar.low(input.readFloat());
        bar.close(input.readFloat());
        bar.volume(input.readInt());
    }
    
    @SneakyThrows
    private void write(Bar bar, DataOutput output) {
        output.writeLong(bar.date());
        output.writeFloat(bar.open());
        output.writeFloat(bar.high());
        output.writeFloat(bar.low());
        output.writeFloat(bar.close());
        output.writeInt(bar.volume());
    }
    
    @Override
    @SneakyThrows
    public void persist(Collection<Bar> bars) {
        for(Bar bar : bars) {
            if(0 < bar.volume() && 0 < bar.open() && 0 < bar.high() && 0 < bar.low() && 0 < bar.close()) {
                final Path path = getPath(bar.symbol(), bar.timeframe());
                if(!Files.exists(path)) Files.createDirectories(path.getParent());
                try(RandomAccessFile file = new RandomAccessFile(path.toFile(), "rw")){
                    file.seek(file.length());
                    write(bar, file);
                }
            }
        }
    }

    @Override
    @SneakyThrows
    public void persist(Symbol symbol, Timeframe timeframe, List<Bar> bars) {
    	if(null == bars || bars.isEmpty()) return;
        final Path path = getPath(symbol, timeframe);
        if(!Files.exists(path)) Files.createDirectories(path.getParent());
        try(RandomAccessFile file = new RandomAccessFile(path.toFile(), "rw")){
            if(BYTES > file.length() || file.readLong() >= bars.get(0).date()) {
                file.setLength(0);
                file.seek(0);
            } else {
                file.seek(file.length());
            }
            bars.forEach(bar -> write(bar, file));
        }
    }

    @Override
    @SneakyThrows
    public List<Bar> findBySymbolAndTimeframe(Symbol symbol, Timeframe timeframe) {
        final List<Bar> bars = new ArrayList<>();
        final Path path = getPath(symbol, timeframe);
        if(!Files.exists(path)) return Collections.emptyList();
        try(RandomAccessFile file = new RandomAccessFile(path.toFile(), "r")){
            while(file.getFilePointer() <= file.length() - BYTES) {
                bars.add(read(symbol, timeframe, file));
            }
        }
        return bars;
    }
    
    @Override
    @SneakyThrows
    public List<Bar> findBySymbolAndTimeframe(Symbol symbol, Timeframe timeframe, int limit) {
        final Path path = getPath(symbol, timeframe);
        if(!Files.exists(path)) return Collections.emptyList();
        try(RandomAccessFile file = new RandomAccessFile(path.toFile(), "r")) {
            final List<Bar> bars = new ArrayList<>((int) Math.min(limit, file.length() / BYTES));
            file.seek(Math.max(0, file.length() - limit * BYTES));
            while(file.getFilePointer() <= file.length() - BYTES) {
                bars.add(read(symbol, timeframe, file));
            }
            return bars;
        }
    }
    
    @Override
    @SneakyThrows
    public BarReader readBySymbolAndTimeframe(Symbol symbol, Timeframe timeframe) {
        final Path path = getPath(symbol, timeframe);
        if(!Files.exists(path)) return BarReader.EMPTY;
        final RandomAccessFile file = new RandomAccessFile(path.toFile(), "r");
        final int count = (int) (file.length() / BYTES);
        return new BarReader() {
            @Override public void close() throws Exception { file.close(); }
            @Override public int size() { return count; }
            @Override public void readInto(Bar bar) {
                read(bar, file);
                bar.symbol(symbol);
                bar.timeframe(timeframe);
            }
        };
    }
    
    @Override
    @SneakyThrows
    public List<Bar> findBySymbolAndTimeframeAfter(Symbol symbol, Timeframe timeframe, long startExclusive) {
        final List<Bar> bars = new ArrayList<>();
        final Path path = getPath(symbol, timeframe);
        if(!Files.exists(path)) return Collections.emptyList();
        try(RandomAccessFile file = new RandomAccessFile(path.toFile(), "r")){
            if(BYTES <= file.length()) {
                long date = Long.MAX_VALUE;
                file.seek(file.length() - BYTES);
                while(0 <= file.getFilePointer() && date > startExclusive) {
                    final Bar bar = read(symbol, timeframe, file);
                    if(bar.date() > startExclusive) {
                        bars.add(bar);
                        date = bar.date();
                        if(2 * BYTES <= file.getFilePointer()) {
                            file.seek(file.getFilePointer() - 2 * BYTES);
                        } else break;
                    }
                }
                Collections.reverse(bars);
            }
            return bars;
        }
        
    }
    
    @Override
    @SneakyThrows
    public long getLastDownloadDate(Symbol symbol, Timeframe timeframe) {
        final Path path = getPath(symbol, timeframe);
        if(!Files.exists(path)) return 0;
        try(RandomAccessFile file = new RandomAccessFile(path.toFile(), "r")){
            file.seek(file.length() - BYTES);
            return file.readLong();
        }
    }
    
    @SneakyThrows
    public long getFirstDownloadDate(Symbol symbol, Timeframe timeframe) {
        final Path path = getPath(symbol, timeframe);
        if(!Files.exists(path)) return 0;
        try(RandomAccessFile file = new RandomAccessFile(path.toFile(), "r")){
            return file.readLong();
        }
    }

}
