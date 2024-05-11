package pl.app.game;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class PacmanGame extends JFrame {
    private int N_BLOCKS;

    public PacmanGame() {

        initUI();
    }

        private final short[] levelData2 = {
            19, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 22,
            21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20,
            21, 0, 24, 24, 24, 24, 24, 24, 16, 24, 24, 24, 16, 24, 24, 24, 24, 24, 0, 20,
            21, 0, 16, 0, 0, 0, 0, 0, 16, 0, 0, 0, 16, 0, 0, 0, 0, 16, 0, 20,
            21, 0, 16, 0, 24, 18, 18, 22, 16, 24, 18, 18, 20, 0, 24, 18, 22, 16, 0, 20,
            21, 0, 16, 0, 0, 17, 16, 20, 16, 0, 17, 16, 20, 0, 0, 17, 20, 16, 0, 20,
            21, 0, 16, 0, 0, 17, 16, 20, 16, 0, 17, 16, 20, 0, 0, 17, 20, 16, 0, 20,
            21, 0, 16, 24, 24, 24, 24, 28, 24, 24, 24, 24, 28, 24, 24, 24, 24, 16, 0, 20,
            21, 0, 16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 16, 0, 20,
            21, 0, 16, 0, 24, 24, 24, 24, 24, 0, 24, 24, 24, 24, 24, 0, 16, 16, 0, 20,
            21, 0, 16, 0, 16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 16, 0, 16, 16, 0, 20,
            21, 0, 16, 0, 16, 0, 24, 18, 22, 0, 19, 18, 22, 0, 16, 0, 16, 16, 0, 20,
            21, 0, 16, 0, 16, 0, 0, 17, 20, 0, 17, 20, 0, 0, 16, 0, 16, 16, 0, 20,
            21, 0, 16, 0, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 0, 16, 16, 0, 20,
            21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20,
            17, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 20,
            25, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 28
    };

    // This map is the representation of 5 binary bits in the decimal system
    // binary: 0, 0, 0, 0, 0 => point present, (walls) boottom, right, up, left
    // ex. 26 (decimal) -> 1 1 0 1 0 (binary) -> point exists, bottom and up
    private final short[] levelData = {
            19, 26, 26, 26, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,
            21, 00, 00, 00, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            21, 00, 00, 00, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            21, 00, 00, 00, 17, 16, 16, 24, 16, 16, 16, 16, 16, 16, 20,
            17, 18, 18, 18, 16, 16, 20, 00, 17, 16, 16, 16, 16, 16, 20,
            17, 16, 16, 16, 16, 16, 20, 00, 17, 16, 16, 16, 16, 24, 20,
            25, 16, 16, 16, 24, 24, 28, 00, 25, 24, 24, 16, 20, 00, 21,
            01, 17, 16, 20, 00, 00, 00, 00, 00, 00, 00, 17, 20, 00, 21,
            01, 17, 16, 16, 18, 18, 22, 00, 19, 18, 18, 16, 20, 00, 21,
            01, 17, 16, 16, 16, 16, 20, 00, 17, 16, 16, 16, 20, 00, 21,
            01, 17, 16, 16, 16, 16, 20, 00, 17, 16, 16, 16, 20, 00, 21,
            01, 17, 16, 16, 16, 16, 16, 18, 16, 16, 16, 16, 20, 00, 21,
            01, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20, 00, 21,
            01, 25, 24, 24, 24, 24, 24, 24, 24, 24, 16, 16, 16, 18, 20,
            9,8, 8, 8, 8, 8, 8, 8, 8, 8, 25, 24, 24, 24, 28
    };
    private void initUI() {
        N_BLOCKS = 15;

        add(new GameEngine(levelData, 15));
        int screenSize = N_BLOCKS * GameEngine.BLOCK_SIZE;
        setTitle("Pacman");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(screenSize+20, screenSize+60);

//        setSize(screenSize+100, screenSize+100);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            var ex = new PacmanGame();
            ex.setVisible(true);
        });
    }
}
