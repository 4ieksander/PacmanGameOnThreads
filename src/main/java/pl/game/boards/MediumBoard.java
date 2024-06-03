package pl.game.boards;

import pl.game.interfaces.IBoard;
import pl.game.subclasses.GameEngine;

public class MediumBoard implements IBoard {
    private final String name = "Large Board";
    private final int N_BLOCKS = 15;
    private final int SCREEN_SIZE = GameEngine.BLOCK_SIZE * N_BLOCKS;

    // This map is the representation of 5 binary bits in the decimal system
    // binary: 0, 0, 0, 0, 0 => point present, (walls) boottom, right, up, left
    // ex. 26 (decimal) -> 1 1 0 1 0 (binary) -> point exists, bottom and up
    private final short[] levelData = {
            19, 18, 18, 18, 18, 18, 18, 18, 26, 18, 18, 18, 18, 18, 22,
            17, 16, 16, 16, 20, 17, 16, 20, 0, 17, 16, 16, 16, 16, 20,
            17, 16, 16, 16, 20, 17, 16, 20, 0, 17, 16, 16, 16, 16, 20,
            17, 16, 16, 16, 20, 17, 16, 20, 0, 17, 16, 24, 24, 24, 20,
            17, 16, 16, 16, 20, 17, 16, 20, 0, 17, 20, 0, 0, 0, 21,
            17, 16, 16, 16, 20, 17, 16, 16, 18, 16, 16, 22, 0, 0, 21,
            17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 18, 18, 20,
            25, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            0, 17, 24, 24, 24, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            0, 17, 18, 18, 18, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            0, 25, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 28
    };

    public MediumBoard() {
    }
    @Override
    public int getSCREEN_SIZE(){
        return SCREEN_SIZE;
    }

    @Override
    public short[]getLevelData(){
        return levelData;
    }

    @Override
    public int getN_BLOCKS(){
        return N_BLOCKS;
    }

    @Override
    public String getName(){
        return name;
    }
}
