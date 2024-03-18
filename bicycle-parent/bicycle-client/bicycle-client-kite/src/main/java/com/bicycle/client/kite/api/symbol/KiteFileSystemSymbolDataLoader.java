package com.bicycle.client.kite.api.symbol;

import com.bicycle.client.kite.api.model.KiteExchange;
import com.bicycle.client.kite.api.model.KiteSymbol;
import com.bicycle.client.kite.utils.Constant;
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
public class KiteFileSystemSymbolDataLoader implements KiteSymbolDataLoader {

    private final KiteSymbolDataLoader delegate;
    
    public KiteFileSystemSymbolDataLoader() {
        this(new KiteHttpSymbolDataLoader());
    }
    
    private Path getPath(KiteExchange kiteExchange) {
        return Paths.get(Constant.HOME, "kite", "symbols", kiteExchange.name().toLowerCase() + ".csv");
    }
    
    @Override
    @SneakyThrows
    public Collection<KiteSymbol> loadByKiteExchange(KiteExchange kiteExchange) {
        final Path path = getPath(kiteExchange);
        if(Files.exists(path) && LocalDateTime.ofInstant(Files.getLastModifiedTime(path)
                .toInstant(), ZoneId.systemDefault()).isAfter(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS))) {
            return Files.lines(path)
                    .filter(text -> !text.contains("-"))
                    .map(KiteSymbol::new).toList();
        } else {
            if(!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            }
            final Collection<KiteSymbol> symbols = delegate.loadByKiteExchange(kiteExchange);
            Files.write(path, symbols.stream()
                    .filter(symbol -> !symbol.getTradingsymbol().contains("-"))
                    .map(Object::toString).toList());
            return symbols;
        }
    }

}
