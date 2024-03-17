package com.bicycle.core.indicator;

import java.util.function.BinaryOperator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IndicatorOperator implements BinaryOperator<Float> {
    
    public static final IndicatorOperator PLUS = new IndicatorOperator("+", (left, right) -> left + right);
    public static final IndicatorOperator MINUS = new IndicatorOperator("-", (left, right) -> left - right);
    public static final IndicatorOperator MULTIPLIED_BY = new IndicatorOperator("*", (left, right) -> left * right);
    public static final IndicatorOperator DIVIDED_BY = new IndicatorOperator("/", (left, right) -> left / right);
    public static final IndicatorOperator POWER_OF = new IndicatorOperator("^", (left, right) -> (float) Math.pow(left, right));
    

    private final String text;
    private final BinaryOperator<Float> delegate;

    @Override
    public Float apply(Float t, Float u) {
        return t.isNaN() || u.isNaN() ? Float.NaN : delegate.apply(t, u);
    }
    
    @Override
    public String toString() {
        return text;
    }

}
