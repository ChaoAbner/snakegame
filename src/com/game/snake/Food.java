package com.game.snake;

import java.util.ArrayList;
import java.util.Arrays;

public class Food implements Parameter {
    private int[] pos = new int[2];

    Food(){
        pos[0] = 15;
        pos[1] = 15;
    }

    public void refresh(ArrayList<int[]> snake) {
        createPos();
        while( isContain(snake,  pos) >= 0){
            createPos();
        }
    }

    public int isContain(ArrayList<int[]> snake, int[] pos) {
        for(int i = 0; i < snake.toArray().length; i++) {
            if (Arrays.equals((int[]) snake.toArray()[i], pos)){
                return i;
            }
        }
        return -1;
    }

    public void createPos() {
        pos[0] = (int) (Math.random() * (ROW - 1));
        pos[1] = (int) (Math.random() * (ROW - 1));
    }

    public int getPosX() {
        return pos[0];
    }

    public int getPosY() {
        return pos[1];
    }

}
