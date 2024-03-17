package com.bicycle.client.shoonya.credentials;

import com.bicycle.client.shoonya.utils.Constant;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.Synchronized;

public class ShoonyaFileSystemCredentialRepository implements ShoonyaCredentialRepository {
    private static final String PATH = Constant.HOME + File.separator + "credentials-shoonya.csv";
    
    private final Map<String, ShoonyaCredentials> cache = new HashMap<>();
    
    @Override 
    public ShoonyaCredentials findByPortfolioId(String portfolioId) {
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
    
    private ShoonyaCredentials map(String text) {
        final String[] tokens = text.split(",");
        return ShoonyaCredentials.builder()
                .portoflioId(tokens[0])
                .username(tokens[1])
                .password(tokens[2])
                .pin(tokens[3])
                .imei(tokens[4])
                .version(tokens[5])
                .vendorCode(tokens[6])
                .applicationKey(tokens[7])
                .build();
    }
    
}
