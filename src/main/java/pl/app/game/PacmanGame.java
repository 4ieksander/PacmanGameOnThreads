package pl.app.game;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class PacmanGame extends JFrame {

    public PacmanGame() {

        initUI();
    }

    private void initUI() {

        add(new GameEngine());

        setTitle("Pacman");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(380, 420);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            var ex = new PacmanGame();
            ex.setVisible(true);
        });
    }
}
