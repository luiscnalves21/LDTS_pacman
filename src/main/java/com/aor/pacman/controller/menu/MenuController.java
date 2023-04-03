package com.aor.pacman.controller.menu;

import com.aor.pacman.Game;
import com.aor.pacman.controller.Controller;
import com.aor.pacman.gui.GUI;
import com.aor.pacman.model.menu.EndMenuBuilder;
import com.aor.pacman.model.menu.Menu;
import com.aor.pacman.model.menu.QuitMenuBuilder;
import com.aor.pacman.states.MenuState;

import java.io.IOException;
import java.util.Objects;

public class MenuController extends Controller<Menu> {
    public MenuController(Menu menu) {
        super(menu);
    }

    @Override
    public void step(Game game, GUI.ACTION action, long time) throws IOException {
        if (Objects.equals(getModel().getReference(), "QuitMenu")) game.setState(null);
        switch (action) {
            case UP -> getModel().previousEntry();
            case DOWN -> getModel().nextEntry();
            case SELECT -> {
                if (getModel().isSelected(1)) {
                    game.setState(new MenuState(new QuitMenuBuilder().createMenu()));
                }
                else {
                    try {
                        game.setState(getModel().getNewState(game.getLevel()));
                    }catch(NullPointerException e){
                        game.resetLevel();
                        game.setState(new MenuState(new EndMenuBuilder().createMenu()));
                    }
                }
            }
            case NEXT -> {
                try {
                    getModel().setCurrentEntry(0);
                    game.setState(getModel().getNewState(game.getLevel()));
                }catch(NullPointerException e){
                    game.resetLevel();
                    game.setState(new MenuState(new EndMenuBuilder().createMenu()));
                }
            }
            case START, RESTART -> {
                if (Objects.equals(getModel().getReference(), "StartMenu") || Objects.equals(getModel().getReference(), "EndMenu") || Objects.equals(getModel().getReference(), "FailedLevelMenu")) {
                    try {
                        getModel().setCurrentEntry(0);
                        game.setState(getModel().getNewState(1));
                    } catch (NullPointerException e) {
                        game.resetLevel();
                    }
                }
            }
            case QUIT -> game.setState(new MenuState(new QuitMenuBuilder().createMenu()));
        }
    }
}
