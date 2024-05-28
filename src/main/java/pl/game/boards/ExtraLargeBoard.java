package pl.game.boards;

import pl.game.interfaces.IBoard;
import pl.game.ui.GameEngine;

public class ExtraLargeBoard implements IBoard {
    private final String name = "Extra Large Board";
    private final int N_BLOCKS = 25;  // Zwiększone do 25
    private final int SCREEN_SIZE = GameEngine.BLOCK_SIZE * N_BLOCKS;

    private final short[] levelData = new short[N_BLOCKS * N_BLOCKS];

    public ExtraLargeBoard() {
        for (int i = 0; i < N_BLOCKS; i++) {
            for (int j = 0; j < N_BLOCKS; j++) {
                if (i == 0 || j == 0 || i == N_BLOCKS - 1 || j == N_BLOCKS - 1) {
                    // Kafelki na krawędziach są ścianami
                    //                                           GORA           lewa
                    levelData[i * N_BLOCKS + j] = (short) (i == 0 ? 18 : (j == 0 ? 17 : (i == N_BLOCKS - 1 ? 24 : 20)));
                } else {
                    // Środkowe kafelki są przejściowe z punktami
                    levelData[i * N_BLOCKS + j] = 16;
                }
            }
        }
        // Dodatkowa logika mogłaby tu być zastosowana do generowania bardziej złożonych schematów labiryntu
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