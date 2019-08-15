package com.game.snake;

import javax.swing.*;

public class GameMain extends JFrame implements Parameter {

    public GameMain() {
        Snake snake = new Snake(INIT_LIST);
        this.add(snake);
        this.addKeyListener(snake);

        this.setTitle("贪吃蛇游戏");
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT + BOX_WIDTH);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setVisible(true);

        new Thread(snake, "snake").start();
    }

    public static void main(String[] args) {
        new GameMain();
    }
}
