package com.bicycle.core.bar.dataSource;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import lombok.SneakyThrows;

class DateLocationIndex {
    private static final int BYTES = Long.BYTES + Long.BYTES + Integer.BYTES;
    
    private final Path path;
    
    @SneakyThrows
    DateLocationIndex(Path path) {
        this.path = path.getParent().resolve(path.getFileName() + "-INDEX");
    }
    
    @SneakyThrows
    long location(long date) {
        try(RandomAccessFile file = new RandomAccessFile(path.toFile(), "r")){
            final long location = location(date, file);
            file.seek(location + Long.BYTES);
            return file.readLong();
        }
    }
    
    @SneakyThrows
    int count(long date) {
        try(RandomAccessFile file = new RandomAccessFile(path.toFile(), "r")){
            final long location = location(date, file);
            file.seek(location + Long.BYTES + Long.BYTES);
            return file.readInt();
        }
    }
    
    @SneakyThrows
    int count(long fromInclusive, long toInclusive) {
        try(RandomAccessFile file = new RandomAccessFile(path.toFile(), "r")){
            int count = 0;
            final long fromLocation = location(fromInclusive, file);
            final long toLocation = location(toInclusive, file);
            for(long location = fromLocation; location <= toLocation; location += BYTES) {
                file.seek(location + Long.BYTES + Long.BYTES);
                count += file.readInt();
            }
            return count;
        }
    }
    
    @SneakyThrows
    void set(long date, long location, int count) {
        if(!Files.exists(path)) {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
        }
        final ByteBuffer buffer = ByteBuffer.allocate(BYTES);
        buffer.putLong(date);
        buffer.putLong(location);
        buffer.putInt(count);
        Files.write(path, buffer.array(), StandardOpenOption.WRITE, 
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
    
    @SneakyThrows
    long head() {
        try(RandomAccessFile file = new RandomAccessFile(path.toFile(), "r")){
            return file.readLong();
        }
    }
    
    @SneakyThrows
    long tail() {
        try(RandomAccessFile file = new RandomAccessFile(path.toFile(), "r")){
            file.seek(file.length() - BYTES);
            return file.readLong();
        }
    }
    
    @SneakyThrows
    private long location(long date, RandomAccessFile file) {
        int low = 0;
        int high = ((int) file.length() / BYTES) - 1;
        
        while (low <= high) {
            int mid = (low + high) >>> 1;
            file.seek(mid * BYTES);
            long midVal = file.readLong();

            if (midVal < date)
                low = mid + 1;
            else if (midVal > date)
                high = mid - 1;
            else
                return mid * BYTES; // key found
        }
        
        return high * BYTES;
    }
}
