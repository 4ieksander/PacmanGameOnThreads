package pl.game.ui;

import pl.game.interfaces.IBoard;
import pl.game.subclasses.GameEngine;
import pl.game.subclasses.Style;
import pl.game.ui.components.GameRender;
import pl.game.ui.components.LivesPanel;
import pl.game.ui.components.StatusPanel;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import java.awt.event.ActionEvent;


public class GameFrame extends JFrame implements ActionListener {
    private final GameRender gameRender;
    private final MenuFrame pacmanMenu;
    private final IBoard board;
    private final GameEngine gameEngine;
    private final StatusPanel statusPanel;
    private final LivesPanel livesPanel;
    private JButton endGameButton;


    public GameFrame(IBoard board, MenuFrame pacmanMenu){
        this.board = board;
        this.pacmanMenu = pacmanMenu;
        gameRender = new GameRender(GameEngine.BLOCK_SIZE * board.getN_BLOCKS());
        statusPanel = new StatusPanel();
        livesPanel = new LivesPanel();
        gameEngine = new GameEngine(board.getLevelData(), board.getN_BLOCKS(), statusPanel, livesPanel, gameRender, this);

        initUI();
    }


    private void initUI() {
        JPanel upperPanel = new JPanel(new BorderLayout());
        endGameButton = Style.createButton("End Game");
        endGameButton.setPreferredSize(new Dimension(120, 20));
        endGameButton.addActionListener(e -> endGame());

        upperPanel.add(livesPanel, BorderLayout.WEST);
        upperPanel.add(endGameButton, BorderLayout.EAST);
        upperPanel.setPreferredSize(new Dimension( board.getSCREEN_SIZE(), 30));
        upperPanel.setBackground(Color.BLACK);

        add(upperPanel, BorderLayout.NORTH);
        add(statusPanel, BorderLayout.SOUTH);
        add(gameRender, BorderLayout.CENTER);

        setTitle("Pacman - " + board.getName());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(board.getSCREEN_SIZE()+15, board.getSCREEN_SIZE()+98);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        setLayout(new BorderLayout());

        initBoard();

    }

    public void endGame(){
        gameEngine.stopGameTimer();
        gameEngine.setInGame(false);
        gameEngine.saveScore();
        pacmanMenu.returnToMenu(this);
    }

    private void initBoard() {
        System.out.println("Board size set to: " + gameEngine.getScreenSize() + "x" + gameEngine.getScreenSize());
        addKeyListener(new GameFrame.TAdapter());
        setFocusable(true);
        setBackground(Color.WHITE);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        gameEngine.initGame();
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
    public String getBoardName(){
        return board.getName();
    }
}
