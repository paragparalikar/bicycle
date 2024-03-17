package com.bicycle.backtest;

import com.bicycle.client.yahoo.adapter.YahooSymbolDataProvider;
import com.bicycle.core.bar.Bar;
import com.bicycle.core.bar.BarReader;
import com.bicycle.core.bar.Timeframe;
import com.bicycle.core.bar.dataSource.BarDataSource;
import com.bicycle.core.bar.dataSource.FileSystemBarDataSource;
import com.bicycle.core.symbol.Exchange;
import com.bicycle.core.symbol.repository.CacheSymbolRepository;
import com.bicycle.core.symbol.repository.SymbolRepository;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.ZonedDateTime;
import java.util.Date;
import javax.imageio.ImageIO;

public class Test {

    public static void main1(String[] args) throws Exception {
        final ZonedDateTime from = ZonedDateTime.now().minusDays(101);
        final ZonedDateTime to = ZonedDateTime.now();
        final SymbolRepository symbolRepository = new CacheSymbolRepository(new YahooSymbolDataProvider());
        final BarDataSource barDataSource = new FileSystemBarDataSource(symbolRepository);
        try(BarReader reader = barDataSource.get(Exchange.NSE, Timeframe.D, from, to)){
            long date = 0;
            final Bar bar = new Bar();
            for(int index = 0; index < reader.size(); index++) {
                reader.readInto(bar);
                if(date != bar.date()) {
                    System.out.println(new Date(date = bar.date()));
                }
            }
        }
    }
    
    public static void main(String[] args) throws IOException {
        final Path sourcePath = Paths.get("C:\\data\\bicycle.zip");
        final Path destinationPath = Paths.get("C:\\data\\bicycle.png");
        write(sourcePath, destinationPath);
        read(destinationPath);
    }
    
    private static void read(Path destinationPath) throws IOException {
        final BufferedImage image = ImageIO.read(destinationPath.toFile());
        final DataBufferByte dataBuffer = (DataBufferByte) image.getRaster().getDataBuffer();
        final byte[] imageData = dataBuffer.getData();
        final ByteBuffer byteBuffer = ByteBuffer.wrap(imageData);
        final int fileSize = byteBuffer.getInt();
        final byte[] fileData = new byte[fileSize];
        byteBuffer.get(fileData);
        Files.write(Paths.get("C:\\data\\bicycle1.zip"), fileData, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
    
    
    private static void write(Path sourcePath, Path destinationPath) throws IOException {
        final byte[] fileData = Files.readAllBytes(sourcePath);
        final int dim = (int) (Math.sqrt(fileData.length) + 2);
        
        final BufferedImage image = new BufferedImage(dim, dim, BufferedImage.TYPE_BYTE_GRAY);
        final DataBufferByte dataBuffer = (DataBufferByte) image.getRaster().getDataBuffer();
        final byte[] imageData = dataBuffer.getData();
        final ByteBuffer byteBuffer = ByteBuffer.wrap(imageData);
        byteBuffer.putInt(fileData.length);
        byteBuffer.put(fileData);
        image.flush();
        
        ImageIO.write(image, "png", destinationPath.toFile());
    }

}
