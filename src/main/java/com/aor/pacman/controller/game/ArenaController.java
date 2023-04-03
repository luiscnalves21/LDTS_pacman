package com.aor.pacman.controller.game;

import com.aor.pacman.Game;
import com.aor.pacman.gui.GUI;
import com.aor.pacman.model.game.Pause;
import com.aor.pacman.model.game.arena.Arena;
import com.aor.pacman.model.menu.*;
import com.aor.pacman.states.MenuState;
import com.aor.pacman.viewer.game.PauseViewer;

import java.io.IOException;

public class ArenaController extends GameController {
    private final PacmanController pacmanController;
    private final MonsterController monsterController;
    private final PauseViewer pauseViewer;

    public ArenaController(Arena arena) {
        super(arena);

        this.pacmanController = new PacmanController(arena);
        this.monsterController = new MonsterController(arena);
        this.pauseViewer = new PauseViewer(new Pause());
    }
    private boolean checkChangeState(Game game, GUI.ACTION action) throws IOException {
        if (action == GUI.ACTION.QUIT) {
            game.setState(new MenuState(new QuitMenuBuilder().createMenu()));
            return true;
        }
        else if (action == GUI.ACTION.PAUSE  && getModel().getPaused()) {
            getModel().setPaused(false);
            return true;
        }
        else if (action == GUI.ACTION.PAUSE  || getModel().getPaused()) {
            if(!getModel().getPaused()) getModel().setPaused(true);
            pauseViewer.draw(game.getGUI());
            return true;
        }
        else if (action == GUI.ACTION.BACK) {
            game.setState(new MenuState(new StartMenuBuilder().createMenu()));
            return true;
        }
        else if(getModel().getPacman().getLife() == 0) {
            Menu menu = new FailedLevelMenuBuilder().createMenu();
            game.setState(new MenuState(menu));
            return true;
        }
        else if(getModel().getCoins().isEmpty()){
            game.levelComplete();
            long score = getModel().getPacman().getScore();
            game.setState(new MenuState(new NextLevelMenuBuilder(score, getModel().getLevel()).createMenu()));
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void step(Game game, GUI.ACTION action, long time) throws IOException {
        if(!checkChangeState(game,action)) {
            pacmanController.step(game, action, time);
            monsterController.step(game, action, time);
        }
    }
}
