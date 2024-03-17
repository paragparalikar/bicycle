package com.bicycle.client.shoonya.api.symbol;

import com.bicycle.client.shoonya.api.model.ShoonyaExchange;
import com.bicycle.client.shoonya.api.model.ShoonyaSymbol;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class ShoonyaHttpSymbolDataLoader implements ShoonyaSymbolDataLoader {

    @Override
    @SneakyThrows
    @SuppressWarnings("unused") 
    public Collection<ShoonyaSymbol> loadByShoonyaExchange(ShoonyaExchange shoonyaExchange) {
        final ZipInputStream zis = new ZipInputStream(new URL("https://api.shoonya.com/" + shoonyaExchange.name() + "_symbols.txt.zip").openStream());
        final ZipEntry zipEntry = zis.getNextEntry();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(zis));
        return reader.lines()
                .skip(1)
                .map(ShoonyaSymbol::new)
                .distinct()
                .toList();
    }

}
