package com.bicycle.client.shoonya.api.symbol;

import com.bicycle.client.shoonya.api.model.ShoonyaExchange;
import com.bicycle.client.shoonya.api.model.ShoonyaSymbol;
import com.bicycle.client.shoonya.utils.Constant;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class ShoonyaFileSystemSymbolDataLoader implements ShoonyaSymbolDataLoader {

    private final ShoonyaSymbolDataLoader delegate;
    
    public ShoonyaFileSystemSymbolDataLoader() {
        this(new ShoonyaHttpSymbolDataLoader());
    }
    
    private Path getPath(ShoonyaExchange shoonyaExchange) {
        return Paths.get(Constant.HOME, "symbols", "shoonya", shoonyaExchange.name().toLowerCase() + ".csv");
    }
    
    @Override
    @SneakyThrows
    public Collection<ShoonyaSymbol> loadByShoonyaExchange(ShoonyaExchange shoonyaExchange) {
        final Path path = getPath(shoonyaExchange);
        if(Files.exists(path) && LocalDateTime.ofInstant(Files.getLastModifiedTime(path)
                .toInstant(), ZoneId.systemDefault()).isAfter(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS))) {
            return Files.lines(path).map(ShoonyaSymbol::new).toList();
        } else {
            if(!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            }
            final Collection<ShoonyaSymbol> symbols = delegate.loadByShoonyaExchange(shoonyaExchange);
            Files.write(path, symbols.stream().map(Object::toString).toList());
            return symbols;
        }
    }

}
