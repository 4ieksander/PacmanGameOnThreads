package pl.game.boards;

import pl.game.interfaces.IBoard;
import pl.game.subclasses.GameEngine;

public class SmallBoard implements IBoard {
    private final String name = "Small Board";
    private final int N_BLOCKS = 12;
    private final int SCREEN_SIZE = GameEngine.BLOCK_SIZE * N_BLOCKS;
    // 1 lewa 2 gora 4 prawa 8 dol
    private final short[] levelData = {
            19, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,
            17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            17, 16, 16, 16,  19, 24, 24, 22, 16, 16, 16, 20,
            17, 16, 16, 16, 20, 0,  0,  17, 16, 16, 16, 20,
            17, 16, 16, 16, 20, 0,  0,  17, 16, 16, 16, 20,
            17, 16, 16, 16, 25, 18, 18, 28, 16, 16, 16, 20,
            17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            25, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 28
    };

    public SmallBoard() {
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
