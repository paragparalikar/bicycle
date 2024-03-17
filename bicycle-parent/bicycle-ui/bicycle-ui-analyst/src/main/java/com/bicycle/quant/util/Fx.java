package com.bicycle.quant.util;

import lombok.experimental.UtilityClass;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;
import atlantafx.base.theme.Styles;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

@UtilityClass
public class Fx {
    
    public MenuItem menuItem(String text, Ikon icon) {
        final MenuItem menuItem = new MenuItem(text, new FontIcon(icon));
        menuItem.getStyleClass().addAll(Styles.DENSE, Styles.FLAT, Styles.SMALL);
        return menuItem;
    }
    
    public MenuItem menuItem(String text, Ikon icon, KeyCode keyCode, KeyCombination.Modifier...modifiers) {
        final MenuItem menuItem = menuItem(text, icon);
        menuItem.setAccelerator(new KeyCodeCombination(keyCode, modifiers));
        return menuItem;
    }
    
    public Menu menu(Ikon icon, String text, MenuItem...items) {
        final Menu menu = new Menu(text, new FontIcon(icon));
        menu.getStyleClass().addAll(Styles.DENSE, Styles.FLAT, Styles.SMALL);
        menu.getItems().addAll(items);
        return menu;
    }

    public Button iconButton(Ikon icon, EventHandler<ActionEvent> eventHandler) {
        final Button button = new Button();
        button.getStyleClass().addAll(Styles.DENSE, Styles.FLAT, Styles.SMALL, Styles.BUTTON_ICON, Styles.TEXT_LIGHTER);
        button.setGraphic(new FontIcon(icon));
        button.setOnAction(eventHandler);
        return button;
    }
    
}
