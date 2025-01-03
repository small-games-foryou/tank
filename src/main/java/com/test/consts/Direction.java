package com.test.consts;

import lombok.Getter;

import java.util.Random;

@Getter
public enum Direction {
    UP, DOWN, LEFT, RIGHT;

    public static Direction random() {
        int i = new Random().nextInt(4);
        switch (i) {
            case 0:
                return Direction.UP;
            case 1:
                return Direction.DOWN;
            case 2:
                return Direction.LEFT;
            case 3:
                return Direction.RIGHT;
            default:
                return Direction.DOWN;
        }
    }

    public static Direction random(Direction direction) {
        Direction random = random();
        if (direction.equals(random)) {
            return random(direction);
        }
        return random;
    }
}
