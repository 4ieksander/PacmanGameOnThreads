package pl.game.subclasses;

import pl.game.interfaces.IBoard;

public abstract class AutomaticBoard implements IBoard {
    protected String name;
    protected int N_BLOCKS;
    protected int SCREEN_SIZE;
    protected short[] levelData;

//    public AutomaticBoard() {
//        drawBoard();
//        addLargeSquare(10, 10, 6);
//        addLargeSquare(25, 25, 8);
//        addLargeSquare(15, 30, 5);
//    }

    protected void drawBoard(){
        for (int i = 0; i < N_BLOCKS; i++) {
            for (int j = 0; j < N_BLOCKS; j++) {
                if (i == 0 || j == 0 || i == N_BLOCKS - 1 || j == N_BLOCKS - 1) {
                    // walls on borders of the board
                    levelData[i * N_BLOCKS + j] = (short) (i == 0 ? 18 : (j == 0 ? 17 : (i == N_BLOCKS - 1 ? 24 : 20)));
                } else {
                    // center
                    levelData[i * N_BLOCKS + j] = 16;
                }
            }
        }
        // corners
        levelData[0] = 19;
        levelData[N_BLOCKS - 1] = 22;
        levelData[N_BLOCKS * (N_BLOCKS - 1)] = 25;
        levelData[N_BLOCKS * N_BLOCKS - 1] = 28;
    }

    protected void addLargeSquare(int startX, int startY, int size) {
        for (int i = startY; i < startY + size; i++) {
            for (int j = startX; j < startX + size; j++) {
                if (i == startY && j == startX) {
                    // top-left corner
                    levelData[i * N_BLOCKS + j] = 16;
                } else if (i == startY && j == startX + size - 1) {
                    // top-right corner
                    levelData[i * N_BLOCKS + j] = 16;
                } else if (i == startY + size - 1 && j == startX) {
                    // bottom-left corner
                    levelData[i * N_BLOCKS + j] = 16;
                } else if (i == startY + size - 1 && j == startX + size - 1) {
                    // bottom-right corner
                    levelData[i * N_BLOCKS + j] = 16;
                } else if (i == startY) {
                    // top wall
                    levelData[i * N_BLOCKS + j] = 24;
                } else if (i == startY + size - 1) {
                    // bottom wall
                    levelData[i * N_BLOCKS + j] = 18;
                } else if (j == startX) {
                    // left wall
                    levelData[i * N_BLOCKS + j] = 20;
                } else if (j == startX + size - 1) {
                    // right wall
                    levelData[i * N_BLOCKS + j] = 17;
                } else {
                    // empty space
                    levelData[i * N_BLOCKS + j] = 0;
                }
            }
        }
    }
    protected void initVariables(int nBlocks) {
        this.N_BLOCKS = nBlocks;
        this.SCREEN_SIZE = GameEngine.BLOCK_SIZE * N_BLOCKS;
        this.levelData = new short[N_BLOCKS * N_BLOCKS];
    }

    @Override
    public int getSCREEN_SIZE() {
        return SCREEN_SIZE;
    }

    @Override
    public short[] getLevelData() {
        return levelData;
    }

    @Override
    public int getN_BLOCKS() {
        return N_BLOCKS;
    }

    @Override
    public String getName() {
        return name;
    }

}
