package com.game.snake;

public interface Parameter {
    int WINDOW_WIDTH = 500;
    int WINDOW_HEIGHT = 500;
    int ROW = 25;
    // 蛇的运动速度随着数值增大则加快，范围>0
    int SPEED = 4;
    int BOX_WIDTH = WINDOW_WIDTH / (ROW + 1);
    int[][] INIT_LIST = {{5, 3}, {4, 3}, {3, 3}};
}
