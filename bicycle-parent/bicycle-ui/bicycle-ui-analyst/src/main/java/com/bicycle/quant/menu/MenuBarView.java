package com.bicycle.quant.menu;

import static javafx.scene.input.KeyCode.N;
import static javafx.scene.input.KeyCode.O;
import static javafx.scene.input.KeyCode.P;
import static javafx.scene.input.KeyCode.R;
import static javafx.scene.input.KeyCode.S;
import static javafx.scene.input.KeyCombination.ALT_DOWN;
import static javafx.scene.input.KeyCombination.CONTROL_DOWN;
import static javafx.scene.input.KeyCombination.SHIFT_DOWN;
import static org.kordamp.ikonli.fontawesome5.FontAwesomeRegular.FILE_ALT;
import static org.kordamp.ikonli.fontawesome5.FontAwesomeRegular.FOLDER_OPEN;
import static org.kordamp.ikonli.fontawesome5.FontAwesomeRegular.SAVE;
import static org.kordamp.ikonli.material.Material.GRID_VIEW;
import static org.kordamp.ikonli.material.Material.PLAY_ARROW;
import static org.kordamp.ikonli.material.Material.PLAY_CIRCLE_FILLED;
import static org.kordamp.ikonli.material.Material.PLAY_CIRCLE_OUTLINE;
import static org.kordamp.ikonli.material.Material.SETTINGS;
import com.bicycle.quant.util.Fx;
import lombok.Getter;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import atlantafx.base.theme.Styles;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

@Getter
public class MenuBarView extends MenuBar {
    
    private final MenuItem newMenuItem = Fx.menuItem("_New", FILE_ALT, N, CONTROL_DOWN);
    private final MenuItem openMenuItem = Fx.menuItem("_Open", FOLDER_OPEN, O, CONTROL_DOWN);
    private final MenuItem saveMenuItem = Fx.menuItem("_Save", SAVE, S, CONTROL_DOWN);
    private final MenuItem saveAsMenuItem = Fx.menuItem("Save As...", FontAwesomeSolid.SAVE);
    private  final Menu fileMenu = Fx.menu(FILE_ALT, "_File", newMenuItem, openMenuItem, saveMenuItem, saveAsMenuItem);
    
    private final MenuItem positionsMenuItem = Fx.menuItem("Positions", GRID_VIEW);
    private final MenuItem preferencesMenuItem = Fx.menuItem("_Preferences", SETTINGS, P, CONTROL_DOWN);
    private final Menu windowMenu = Fx.menu(SETTINGS, "_Window", positionsMenuItem, new SeparatorMenuItem(), preferencesMenuItem);
    
    private final MenuItem executeMenuItem = Fx.menuItem("_Execute", PLAY_ARROW, R, CONTROL_DOWN);
    private final MenuItem optimizeMenuItem = Fx.menuItem("Optimize", PLAY_CIRCLE_OUTLINE, R, CONTROL_DOWN, SHIFT_DOWN);
    private final MenuItem walkForwardMenuItem = Fx.menuItem("_WalkForward", PLAY_CIRCLE_FILLED, R, CONTROL_DOWN, SHIFT_DOWN, ALT_DOWN);
    private final Menu runMenu = Fx.menu(PLAY_ARROW, "_Run", executeMenuItem, optimizeMenuItem, walkForwardMenuItem);
    
    public MenuBarView() {
        getMenus().addAll(fileMenu, runMenu, windowMenu);
        getStyleClass().addAll(Styles.DENSE, Styles.FLAT, Styles.SMALL);
    }

}
