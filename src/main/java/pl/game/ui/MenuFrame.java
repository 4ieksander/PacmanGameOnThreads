package pl.game.ui;

import pl.game.boards.*;
import pl.game.interfaces.IBoard;
import pl.game.exceptions.BoardDoesNotExistException;
import pl.game.subclasses.Style;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuFrame extends JFrame {

    private IBoard board;
    private JPanel cards; // panel that uses CardLayout
    final static String MENUPANEL = "Menu Panel";
    final static String GAMEPANEL = "Game Panel";

    public MenuFrame() {
        setTitle("Pacman");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center the window
        initUI();
    }

    public void returnToMenu(JFrame gameFrame) {
        gameFrame.dispose();
        setVisible(true);
    }


    private void initUI() {
        cards = new JPanel(new CardLayout());

        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(Style.BACKGROUND_COLOR);
        menuPanel.setForeground(Style.TEXT_COLOR);
        menuPanel.setFont(Style.TEXT_FONT);

        JButton newGameButton = Style.createButton("New Game");
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showBoardSizeOptions();
            }
        });

        JButton highScoreButton = Style.createButton("High Score");
        highScoreButton.addActionListener(e -> {
            EventQueue.invokeLater(() -> {
                var scoreWindow = new ScoreFrame();
                scoreWindow.setVisible(true);
            });
        });

        JButton exitButton = Style.createButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));

        JPanel gamePanel = new JPanel();

        menuPanel.add(newGameButton, MENUPANEL);
        menuPanel.add(highScoreButton, null);
        menuPanel.add(exitButton, MENUPANEL);

        cards.add(menuPanel, MENUPANEL);
        cards.add(gamePanel, GAMEPANEL);

        getContentPane().add(cards, BorderLayout.CENTER);
    }


    private void showBoardSizeOptions() {
        JFrame boardSizeFrame = new JFrame("Choose Board Size");
        boardSizeFrame.setSize(350, 400);
        boardSizeFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        boardSizeFrame.setLocationRelativeTo(null);


        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 10, 10));

        panel.setBackground(Style.BACKGROUND_COLOR);
        panel.setForeground(Style.TEXT_COLOR);
        panel.setFont(Style.TEXT_FONT);

        String[] boardSizes = {"Small", "Medium", "Large", "Extra Large", "Huge"};
        for (String size : boardSizes) {
            JButton sizeButton = Style.createButton(size);
            sizeButton.addActionListener(e -> {
                selectBoard(size);
                boardSizeFrame.dispose();
            });
            panel.add(sizeButton);
        }

        boardSizeFrame.add(panel);
        this.setVisible(false);
        boardSizeFrame.setVisible(true);
    }


    private void selectBoard(String size) {
        try {
            switch (size) {
                case "Small":
                    board = new SmallBoard();
                    break;
                case "Medium":
                    board = new MediumBoard();
                    break;
                case "Large":
                    board = new LargeBoard();
                    break;
                case "Extra Large":
                    board = new ExtraLargeBoard();
                    break;
                case "Huge":
                    board = new HugeBoard();
                    break;
                default:
                    throw new BoardDoesNotExistException("The selected board size does not exist: " + size);
            }
            runGame();
        } catch (BoardDoesNotExistException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(e.getMessage());
        }
    }

    
    private void runGame(){
        EventQueue.invokeLater(() -> {

            var ex = new GameFrame(board, this);
            ex.setVisible(true);
        });
    }
};
