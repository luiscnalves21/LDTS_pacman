package com.aor.pacman.model.game.elements;

import com.aor.pacman.model.Position;

public class Movable extends Element {
    private final Position startingPosition;
    public Movable(int x, int y) {
        super(x, y);
        this.startingPosition = new Position(x,y);
    }

    public void resetPosition() {
        this.setPosition(startingPosition);
    }
}
