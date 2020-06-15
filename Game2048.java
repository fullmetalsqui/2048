package com.javarush.games.game2048;

import com.javarush.engine.cell.*;

public class Game2048 extends Game {
    private static final int SIDE = 4;
    private int[][] gameField;
    private boolean isGameStopped = false;
    private int score = 0;

    private void createGame(){
        gameField = new int[SIDE][SIDE];
        createNewNumber();
        createNewNumber();
    }
    private void drawScene(){
        for (int i = 0; i < SIDE; i++){
            for (int j = 0; j < SIDE; j++){
                setCellColoredNumber(i, j, gameField[j][i]);
            }
        }
    }
    private void createNewNumber(){
        if (getMaxTileValue() == 2048)
            win();
        int x = getRandomNumber(SIDE);
        int y = getRandomNumber(SIDE);
        if (gameField[y][x] == 0){
            if (getRandomNumber(10) == 9){
                gameField[y][x] = 4;
            }
            else gameField[y][x] = 2;
        } else createNewNumber();
    }
    private Color getColorByValue(int value){
        switch (value){
            case 2: return Color.LIGHTPINK;
            case 4: return Color.VIOLET;
            case 8: return Color.BLUEVIOLET;
            case 16: return Color.SKYBLUE;
            case 32: return Color.FORESTGREEN;
            case 64: return Color.DARKSEAGREEN;
            case 128: return Color.YELLOWGREEN;
            case 256: return Color.LIGHTGOLDENRODYELLOW;
            case 512: return Color.ORANGE;
            case 1024: return Color.LIGHTSLATEGREY;
            case 2048: return Color.TOMATO;
        }
        return Color.ANTIQUEWHITE;
    }
    private void setCellColoredNumber(int x, int y, int value){
        if (value != 0)
        setCellValueEx(x,y,getColorByValue(value), String.valueOf(value));
        else setCellValueEx(x,y,getColorByValue(value), "");
    }
    private boolean compressRow(int[] row){
        boolean isCompressed = false;
        int x;
        for (int i = 0; i < row.length; i++){
            if(row[i] == 0){
                x = i;
                for (int j = x; j < row.length; j++){
                    if ( row[j] != 0) {
                        row[x] = row[j];
                        row[j] = 0;
                        x++;
                        isCompressed = true;
                    }
                }
            }
        }
        return isCompressed;
    }
    private boolean mergeRow(int[] row){
        boolean isMerged = false;
        for (int i = 0; i < row.length - 1; i++){
            if (row[i] == row[i+1] && row[i] != 0){
                row[i] = row[i]+row[i+1];
                row[i+1] = 0;
                score += row[i]+row[i+1];
                setScore(score);
                isMerged = true;
            }
        }
        return isMerged;
    }
    private void moveLeft(){
        boolean change = false;
        for (int i = 0; i < SIDE; i++) {
            if (compressRow(gameField[i])) change = true;
            if (mergeRow(gameField[i])) change = true;
            if (compressRow(gameField[i])) change = true;
        }
        if (change) createNewNumber();
    }
    private void moveRight(){
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
    }
    private void moveUp(){
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
    }
    private void moveDown(){
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
    }
    private void rotateClockwise(){
        gameField = turn(gameField);
    }
    private int[][] turn(int[][] array){
        int[][] result = new int[SIDE][SIDE];
        for (int i = 0; i < array.length; i++){
            for (int j = 0; j < array.length; j++){
                result[i][j] = array[array.length-1-j][i];
            }
        }
        return result;
    }
    private int getMaxTileValue(){
        int max = gameField[0][0];
        for (int i = 0; i < SIDE; i++){
            for (int j = 0; j < SIDE; j++){
                if (max < gameField[j][i]){
                    max = gameField[j][i];
                }
            }
        }
        return max;
    }
    private void win(){
        isGameStopped = true;
        showMessageDialog(Color.DARKRED, "gg wp", Color.LIGHTGOLDENRODYELLOW, 72);
    }
    private boolean canUserMove(){
        for (int x = 0; x < SIDE; x++){
            for (int y = 0; y < SIDE; y++){
                if (gameField[y][x] == 0)
                    return true;
                if (y < SIDE -1){
                    if (gameField[y][x] == gameField[y+1][x])
                        return true;
                }
                if (x < SIDE -1){
                    if (gameField[y][x] == gameField[y][x+1])
                        return true;
                }
            }
        }
        return false;
    }
    private void gameOver(){
        isGameStopped = true;
        showMessageDialog(Color.DARKRED, "7king looser", Color.LIGHTGOLDENRODYELLOW, 72);
    }

    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
        drawScene();
    }

    @Override
    public void onKeyPress(Key key) {
        if (!isGameStopped){
            if (!canUserMove()) {
                gameOver();
            } else {
                switch (key){
                    case RIGHT:
                        moveRight();
                        drawScene();
                        break;
                    case LEFT:
                        moveLeft();
                        drawScene();
                        break;
                    case DOWN:
                        moveDown();
                        drawScene();
                        break;
                    case UP:
                        moveUp();
                        drawScene();
                        break;
                }
            }
        } else if (key.equals(Key.SPACE)){
            isGameStopped = false;
            createGame();
            drawScene();
            score = 0;
            setScore(score);
        }
    }
}
