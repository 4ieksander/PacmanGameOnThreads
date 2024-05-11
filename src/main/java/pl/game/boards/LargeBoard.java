package pl.game.boards;

import pl.game.ui.GameEngine;
import pl.game.interfaces.IBoard;

public class LargeBoard implements IBoard {
    private String name = "Large Board";
    private final int N_BLOCKS = 15;
    private final int SCREEN_SIZE = GameEngine.BLOCK_SIZE * N_BLOCKS;
    private final short[] levelData = {
            19, 18, 18, 18, 18, 18, 18, 18, 26, 18, 18, 18, 18, 18, 22,
            17, 16, 16, 16, 4, 1, 16, 20, 0, 17, 16, 16, 16, 16, 20,
            17, 16, 16, 16, 4, 1, 16, 20, 0, 17, 16, 16, 16, 16, 20,
            17, 16, 16, 16, 4, 1, 16, 20, 0, 17, 16, 24, 24, 24, 20,
            17, 16, 16, 16, 4, 1, 16, 20, 0, 17, 20, 0, 0, 0, 21,
            17, 16, 16, 16, 4, 1, 16, 16, 18, 16, 16, 22, 0, 0, 21,
            17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 18, 18, 20,
            25, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            0, 17, 18, 18, 18, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            0, 25, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 28


    };

    @Override
    public short[]getLevelData(){
        return levelData;
    }

    @Override
    public int getN_BLOCKS(){
        return N_BLOCKS;
    }

    @Override
    public int getSCREEN_SIZE(){
        return SCREEN_SIZE;
    }

    @Override
    public String getName(){
        return name;
    }
}
