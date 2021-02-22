package com.javarush.games.game2048;

import com.javarush.engine.cell.*;

public class Game2048 extends Game {

    private static final int SIDE = 4;
    private boolean isGameStopped = false;
    private int score = 0;
    private int[][] gameField =
           /* {
            {  2,    2,    2,  2},
            { 4,   4, 4, 4},
            {8, 8, 8,   8},
            {  0,    0,    0,  0}
    };*/
       new int[SIDE][SIDE];

    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
        drawScene();
    }

    @Override
    public void onKeyPress(Key key) {
        if (!canUserMove()) {
            gameOver();
            return;
        }
       /* if (Key.ESCAPE.equals(key)) {
        }*/
        if (isGameStopped) {
            if (Key.SPACE.equals(key)) {
                isGameStopped = false;
                createGame();
                score = 0;
                setScore(score);
                drawScene();
            }
        } else {
            if (Key.LEFT.equals(key)) {
                moveLeft();
            }
            if (Key.RIGHT.equals(key)) {
                moveRight();
            }
            if (Key.UP.equals(key)) {
                moveUp();
            }
            if (Key.DOWN.equals(key)) {
                moveDown();
            }
            drawScene();
        }
    }

    private void createGame() {
        gameField = new int[SIDE][SIDE];
        createNewNumber();
        createNewNumber();
    }

    private boolean canUserMove() {
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                if (gameField[i][j] == 0 ||
                        (i < SIDE - 1 && gameField[i][j] == gameField[i + 1][j]) ||
                        (j < SIDE - 1 && gameField[i][j] == gameField[i][j + 1])
                )
                    return true;
            }
        }
        return false;
    }

    private void createNewNumber() {
        int x, y;
        do {
            x = getRandomNumber(SIDE);
            y = getRandomNumber(SIDE);
        } while (gameField[x][y] != 0);
        gameField[x][y] = (getRandomNumber(10) == 9 ? 4 : 2);

        if (getMaxTileValue() == 2048) {
            win();
        }
    }

    private void drawScene() {
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                setCellColoredNumber(j, i, gameField[i][j]);
            }
        }
    }

    private void setCellColoredNumber(int x, int y, int value) {
        setCellValueEx(x, y, getColorByValue(value), value == 0? "" : String.valueOf(value));
    }

    private Color getColorByValue(int value) {
        if (value == 2)
            return Color.LIGHTBLUE;
        if (value == 4)
            return  Color.AQUA;
        if (value == 8)
            return  Color.BLUE;
        if (value == 16)
            return  Color.PURPLE;
        if (value == 32)
            return  Color.RED;
        if (value == 64)
            return  Color.ORANGE;
        if (value == 128)
            return  Color.YELLOW;
        if (value == 256)
            return  Color.IVORY;
        if (value == 512)
            return  Color.LIGHTGREY;
        if (value == 1024)
            return  Color.GRAY;
        if (value == 2048)
            return  Color.LIGHTGREEN;
        return Color.GREEN;
    }

    private boolean compressRow(int[] row) {
        boolean result = false;
        for (int i = 0; i < row.length - 1; i++) {
            for (int j = i; j < row.length; j++) {
                if (row[i] == 0 && row[j] != 0) {
                    row[i] = row[j];
                    row[j] = 0;
                    result = true;
                }
            }
        }
        return result;
    }

    private boolean mergeRow(int[] row) {
        boolean result = false;
        for (int i = 0; i < row.length - 1; i++) {
            for (int j = i + 1; j < row.length; j++) {
                if (row[j] == row[j - 1] && row[j] != 0) {
                    row[j - 1] += row[j];
                    row[j] = 0;
                    score += row[j - 1];
                    setScore(score);
                    result = true;
                }
            }
        }
        return result;
    }

    private void rotateClockwise() {
        int[][] matrix = new int[SIDE][SIDE];
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                matrix[j][SIDE - i - 1] = gameField[i][j];
            }
        }
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                gameField[i][j] = matrix[i][j];
            }
        }
    }

    private void moveLeft() {
        boolean a = false,
                b = false,
                c = false;
        for (int i = 0; i < SIDE; i++) {
            a = compressRow(gameField[i]) || a;
            b = mergeRow(gameField[i]) || b;
            c = compressRow(gameField[i]) || c;
        }
        if (a || b || c) {
            createNewNumber();
        }
    }
    private void moveRight() {
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
    }
    private void moveUp() {
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
    }
    private void moveDown() {
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
    }

    private int getMaxTileValue() {
        int maxTileValue = 0;
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                if (gameField[i][j] > maxTileValue) {
                    maxTileValue = gameField[i][j];
                }
            }
        }
        return maxTileValue;
    }

    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.PINK,
                "You are the Winner!!!", Color.BLACK, 75);
    }

    private void gameOver() {
        isGameStopped = true;
        showMessageDialog(Color.RED,
                "You are the Loser!", Color.BLACK, 75);
    }
}
