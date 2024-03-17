package com.bicycle.quant;

import java.util.Locale;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public interface Constant {
    
    String FONT_FAMILTY = "Yu Gothic";
    Locale LOCALE = Locale.of("en", "IN");
    Font FONT_NORMAL = Font.font(FONT_FAMILTY, FontWeight.NORMAL, 12);
    Font FONT_BOLD = Font.font(FONT_FAMILTY, FontWeight.BOLD, 12);
    Color COLOR_POSITIVE = Color.GREEN;
    Color COLOR_NEGATIVE = Color.RED;
    Color COLOR_NEUTRAL = Color.BLACK;
    

}
