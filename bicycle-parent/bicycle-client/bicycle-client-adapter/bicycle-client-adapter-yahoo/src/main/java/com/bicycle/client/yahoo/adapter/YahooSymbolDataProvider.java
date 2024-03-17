package com.bicycle.client.yahoo.adapter;

import com.bicycle.core.symbol.Exchange;
import com.bicycle.core.symbol.Symbol;
import com.bicycle.core.symbol.provider.SymbolDataProvider;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.SneakyThrows;

public class YahooSymbolDataProvider implements SymbolDataProvider {
    
    @Override
    @SneakyThrows
    public Collection<Symbol> findByExchange(Exchange exchange) {
        if(!Exchange.NSE.equals(exchange)) throw new IllegalArgumentException("Only NSE exchange is supported");
        final List<Symbol> symbols = new ArrayList<>();
        try(final InputStream inputStream = getClass().getClassLoader().getResourceAsStream("YAHOO_EQ.txt");
            final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))){
            int index = 0;
            String code = null;
            while(null != (code = reader.readLine())) {
                symbols.add(toSymbol(index++, code));
            }
        }
        return symbols;
    }

    private Symbol toSymbol(int index, String code) {
        return Symbol.builder()
                .token(index)
                .code(code)
                .name(code)
                .exchange(Exchange.NSE)
                .lotSize(1)
                .tickSize(0.05f)
                .type("EQ")
                .build();
    }
    
}
