package pl.game.ui;

import pl.game.boards.MediumBoard;
import pl.game.interfaces.IBoard;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PacmanGame extends JFrame implements ActionListener {
    private IBoard board;
    private GameEngine gameEngine;
    private StatusPanel statusPanel;
    private final GameRender gameRender;
    private LivesPanel livesPanel;



    public PacmanGame(IBoard board) {
        this.board = board;

        gameRender = new GameRender(100);

        gameEngine = new GameEngine(board.getLevelData(), board.getN_BLOCKS(), statusPanel, livesPanel, gameRender);
        statusPanel = new StatusPanel();
        livesPanel = new LivesPanel();

        initUI();
    }


    private void initUI() {
        add(livesPanel, BorderLayout.SOUTH);
        add(statusPanel, BorderLayout.NORTH); // Position it at the top or wherever it fits best
        add(gameRender, BorderLayout.CENTER);
        int screen = GameEngine.BLOCK_SIZE * board.getN_BLOCKS();
        System.out.println(board.getN_BLOCKS());
        System.out.println(screen);
        setTitle("Pacman - " + board.getName());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(board.getSCREEN_SIZE()+20, board.getSCREEN_SIZE()+60);
        setLocationRelativeTo(null);
        setVisible(true);
        setLayout(new BorderLayout());

        initBoard();

    }
    private void initBoard() {
        System.out.println("Board size set to: " + gameEngine.getScreenSize() + "x" + gameEngine.getScreenSize());
//        addKeyListener(new GameEngine.TAdapter());
//        setFocusable(true);
//        setBackground(Color.WHITE);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        gameEngine.initGame();
    }
    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            var ex = new PacmanGame(new MediumBoard());
            ex.setVisible(true);
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        gameRender.repaint();
    }


    public class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if (gameEngine.isInGame()) {
                switch (key) {
                    case KeyEvent.VK_LEFT:
                        gameEngine.setTempDirX(-1);
                        gameEngine.setTempDirY(0);
                        break;
                    case KeyEvent.VK_RIGHT:
                        gameEngine.setTempDirX(1);
                        gameEngine.setTempDirY(0);
                        break;
                    case KeyEvent.VK_UP:

                        gameEngine.setTempDirX(0);
                        gameEngine.setTempDirY(-1);
                        break;
                    case KeyEvent.VK_DOWN:
                        gameEngine.setTempDirX(0);
                        gameEngine.setTempDirY(1);
                        break;
                    default:
                        break;
                }
            } else {
                if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_ENTER) {
                    gameEngine.setInGame(true);
                    gameEngine.initGame();
                }
            }
        }



        @Override
        public void keyReleased(KeyEvent e) {

            int key = e.getKeyCode();

            if (key == Event.LEFT || key == Event.RIGHT
                    || key == Event.UP || key == Event.DOWN) {
                gameEngine.setTempDirX(0);
                gameEngine.setTempDirY(0);
            }
        }
    }



}
