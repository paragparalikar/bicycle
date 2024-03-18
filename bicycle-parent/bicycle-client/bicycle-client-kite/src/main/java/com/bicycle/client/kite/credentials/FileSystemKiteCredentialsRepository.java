package com.bicycle.client.kite.credentials;

import com.bicycle.client.kite.utils.Constant;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.Synchronized;

public class FileSystemKiteCredentialsRepository implements KiteCredentialsRepository {
    private static final String PATH = Constant.HOME + 
    		File.separator + "kite" + 
    		File.separator + "credentials-kite.csv";
    
    private final Map<String, KiteCredentials> cache = new HashMap<>();
    
    @Override 
    public KiteCredentials findByPortfolioId(String portfolioId) {
        if(cache.isEmpty()) loadAll();
        return cache.get(portfolioId);
    }
    
    @SneakyThrows
    @Synchronized
    private void loadAll() {
        Files.lines(Paths.get(PATH))
            .map(this::map)
            .forEach(credentials -> cache.put(credentials.portoflioId(), credentials));
    }
    
    private KiteCredentials map(String text) {
        final String[] tokens = text.split(",");
        return KiteCredentials.builder()
                .portoflioId(tokens[0])
                .username(tokens[1])
                .password(tokens[2])
                .pin(tokens[3])
                .build();
    }
    
}
