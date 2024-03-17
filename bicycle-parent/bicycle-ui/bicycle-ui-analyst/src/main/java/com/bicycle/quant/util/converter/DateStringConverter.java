package com.bicycle.quant.util.converter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.SneakyThrows;
import javafx.util.StringConverter;

public class DateStringConverter extends StringConverter<Number> {
    
    private final DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

    @Override
    public String toString(Number object) {
        return null == object ? "" : dateFormat.format(new Date(object.longValue()));
    }

    @Override
    @SneakyThrows
    public Number fromString(String string) {
        return null == string ? null : dateFormat.parse(string).getTime();
    }

}
