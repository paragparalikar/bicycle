package com.bicycle.core.bar.provider;

import com.bicycle.core.bar.Bar;
import com.bicycle.core.bar.provider.query.BarQuery;
import com.bicycle.core.bar.provider.query.BarQueryTransformer;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SmartBarDataProvider implements BarDataProvider {
    
    private final BarDataProvider delegate;
    private final BarQueryTransformer barQueryTransformer = new BarQueryTransformer();

    @Override
    public List<Bar> get(BarQuery barQuery) {
        return barQueryTransformer.transform(barQuery)
            .map(delegate::get)
            .orElse(Collections.emptyList());
    }

}
