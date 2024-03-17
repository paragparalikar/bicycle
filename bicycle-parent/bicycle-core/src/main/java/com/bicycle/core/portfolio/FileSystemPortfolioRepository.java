package com.bicycle.core.portfolio;

import com.bicycle.Constant;
import com.bicycle.core.broker.BrokerType;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import lombok.SneakyThrows;

public class FileSystemPortfolioRepository implements PortfolioRepository {
    
    private Path getPath() {
        return Paths.get(Constant.HOME, "portfolios.csv");
    }

    @Override
    @SneakyThrows
    public Collection<Portfolio> findAll() {
        final Path path = getPath();
        final Collection<Portfolio> portfolios = Files.exists(path) ?
                Files.lines(path).map(this::map).toList() : Collections.emptyList();
        return portfolios;
    }
    
    private Portfolio map(String text) {
        final String[] tokens = text.split(",");
        return Portfolio.builder()
                .id(tokens[0])
                .broker(BrokerType.valueOf(tokens[1]))
                .enabledForTrading(Boolean.parseBoolean(tokens[2]))
                .build();
    }

}
