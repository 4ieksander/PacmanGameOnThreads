package pl.game.boards;

import pl.game.interfaces.IBoard;
import pl.game.ui.GameEngine;

public class HugeBoard implements IBoard {
    private final String name = "Extra Large Board";
    private final int N_BLOCKS = 40;
    private final int SCREEN_SIZE = GameEngine.BLOCK_SIZE * N_BLOCKS;

    private final short[] levelData = new short[N_BLOCKS * N_BLOCKS];

    public HugeBoard() {
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