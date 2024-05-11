package pl.game.ui;

import pl.game.boards.LargeBoard;
import pl.game.interfaces.IBoard;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class PacmanGame extends JFrame {
    private IBoard board;

    public PacmanGame(IBoard board) {
        this.board = board;
        initUI();
    }

    private void initUI() {
        add(new GameEngine(board.getLevelData(), board.getN_BLOCKS()));
        setTitle("Pacman - " + board.getName());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(board.getSCREEN_SIZE()+20, board.getSCREEN_SIZE()+60);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            var ex = new PacmanGame(new LargeBoard());
            ex.setVisible(true);
        });
    }
}
