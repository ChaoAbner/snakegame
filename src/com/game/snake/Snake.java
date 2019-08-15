package com.game.snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Snake extends JPanel implements Parameter, KeyListener, Runnable {

    private boolean isStart = true;
    private boolean isUpdate = false;
    private int score = 0;
    private String derection = "right";
    private Food food = new Food();
    private JLabel showScoreBar = new JLabel();

    private ArrayList <int[]> snake = new ArrayList<int[]>();

    Snake(int[][] initList) {
        initSnake(initList);
        showScoreBar.setFont(new Font("alias", Font.BOLD, 15));
        showScoreBar.setForeground(Color.BLUE);
        this.setBackground(Color.white);
        this.add(showScoreBar);
    }

    private void initSnake(int[][] initList) {
        for (int i = 0; i < initList.length; i++) {
            snake.add(initList[i]);
        }
    }

    // 蛇的运动
    private void move() {
        int[] newHead = new int[2];
        int [][] tempSnake = (int [][]) snake.toArray(new int[0][0]);
        int[] oldHead = tempSnake[0];
        int x = oldHead[0];
        int y = oldHead[1];
        switch (derection) {
            case "right": {
                this.eat(0, 1);
                newHead[0] = x + 1;
                newHead[1] = y;
            } break;
            case "left": {
                this.eat(0, -1);
                newHead[0] = x - 1;
                newHead[1] = y;
            } break;
            case "up": {
                this.eat(1, -1);
                newHead[0] = x;
                newHead[1] = y - 1;
            } break;
            case "down": {
                this.eat(1, 1);
                newHead[0] = x;
                newHead[1] = y + 1;
            } break;
        }

        this.updateSnake(newHead);
        repaint();
    }

    private void updateSnake(int[] newHead) {
        if( !isDie(newHead) ){
            snake.add(0, newHead);
            snake.remove(snake.size() - 1);
            isUpdate = true;
        }
    }

    private boolean isDie(int[] newHead) {
        if (food.isContain(snake, newHead) > 0 ||
                (newHead[0] < 0 || newHead[0] >= ROW ||
                        newHead[1] < 0 || newHead[1] >= ROW)){
            isStart = false;
            return true;
        }
        return false;
    }

    private void eat(int index, int var) {
        int [][] tempSnake = (int [][]) snake.toArray(new int[0][0]);
        if (tempSnake[0][0] == food.getPosX() && tempSnake[0][1] == food.getPosY()) {
            // 蛇身变长
            this.insert(food.getPosX(), food.getPosY(), index, var);
            // 更新食物位置
            food.refresh(snake);
            score += 1;
        }
    }

    private void insert(int posX, int posY, int index, int var) {
        int[] pos = new int[2];
        if(index == 1) {
            pos[0] = posX;
            pos[1] = posY + var;
        } else {
            pos[0] = posX + var;
            pos[1] = posY;
        }
        snake.add(0, pos);
    }

    public void paint(Graphics g) {
        super.paint(g);
        int [][] tempSnake = (int [][]) snake.toArray(new int[0][0]);
        // 画网格
        for (int i = 0; i < ROW; i++) {
            g.drawLine(0, BOX_WIDTH * i, WINDOW_WIDTH, i * BOX_WIDTH);
            g.drawLine(BOX_WIDTH * i, 0, BOX_WIDTH * i, WINDOW_HEIGHT);
        }
        // 画蛇身体
        for (int i = 0; i < snake.size(); i++) {
            for (int j = 0; j < 2; j++) {
                if (i == 0){
                    g.setColor(Color.RED);
                } else {
                    g.setColor(Color.BLACK);
                }
                g.fillRect(tempSnake[i][0] * BOX_WIDTH,
                        tempSnake[i][1] * BOX_WIDTH, BOX_WIDTH, BOX_WIDTH);
            }
        }
        // 画食物
        g.setColor(Color.GREEN);
        g.fillRect(food.getPosX() * BOX_WIDTH,
                food.getPosY() * BOX_WIDTH, BOX_WIDTH, BOX_WIDTH);

        showScoreBar.setText("当前分数：" + score);

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(isUpdate){
            if(e.getKeyCode() == KeyEvent.VK_UP && derection != "down"){
                derection = "up";
            }
            if(e.getKeyCode() == KeyEvent.VK_DOWN && derection != "up"){
                derection = "down";
            }
            if(e.getKeyCode() == KeyEvent.VK_LEFT && derection != "right"){
                derection = "left";
            }
            if(e.getKeyCode() == KeyEvent.VK_RIGHT && derection != "left"){
                derection = "right";
            }
        }
        isUpdate = false;
    }

    public void run() {
        while (isStart){
            this.move();
            try {
                Thread.sleep(450 / SPEED);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { }

    @Override
    public void keyTyped(KeyEvent e) { }
}
