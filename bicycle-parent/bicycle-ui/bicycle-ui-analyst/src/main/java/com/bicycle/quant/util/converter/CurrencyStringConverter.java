package com.bicycle.quant.util.converter;

import java.text.NumberFormat;
import java.util.Locale;
import lombok.SneakyThrows;
import javafx.util.StringConverter;

public class CurrencyStringConverter extends StringConverter<Double> {
    
    public static final CurrencyStringConverter INSTANCE = new CurrencyStringConverter();
    
    private final NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.of("hi", "IN"));

    @Override
    public String toString(Double object) {
        return null == object ? null : numberFormat.format(object.doubleValue());
    }

    @Override
    @SneakyThrows
    public Double fromString(String string) {
        return null == string ? null : numberFormat.parse(string).doubleValue();
    }

}
