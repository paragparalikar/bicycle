package com.bicycle.client.kite.api.symbol;

import com.bicycle.client.kite.KiteConstant;
import com.bicycle.client.kite.api.model.KiteExchange;
import com.bicycle.client.kite.api.model.KiteSymbol;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class KiteHttpSymbolDataLoader implements KiteSymbolDataLoader {

    @Override
    @SneakyThrows
    public Collection<KiteSymbol> loadByKiteExchange(KiteExchange kiteExchange) {
        final InputStream is = new URI(KiteConstant.URL_INSTRUMENTS + kiteExchange.name()).toURL().openStream();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        final List<KiteSymbol> kiteSymbols = reader.lines()
                .skip(1)
                .map(KiteSymbol::new)
                .distinct()
                .toList();
        log.info("Downloaded {} kite symbols", kiteSymbols.size());
        return kiteSymbols;
    }

}
