package pl.game;

import pl.game.boards.LargeBoard;
import pl.game.game.boards.*;
import pl.game.ui.PacmanGame;
import pl.game.interfaces.IBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PacmanMenu extends JFrame {

    private IBoard board;
    private JPanel cards; // panel that uses CardLayout
    final static String MENUPANEL = "Menu Panel";
    final static String GAMEPANEL = "Game Panel";

    public PacmanMenu() {
        setTitle("Pacman");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center the window
        initUI();
    }

    private void initUI() {
        cards = new JPanel(new CardLayout());

        JPanel menuPanel = new JPanel();
        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showBoardSizeOptions();
            }
        });

        JButton highScoreButton = new JButton("High Score");

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));

        JPanel gamePanel = new JPanel();
        gamePanel.setBackground(Color.BLACK);
        // Initialize your game panel here


        menuPanel.add(newGameButton, MENUPANEL);
        menuPanel.add(highScoreButton, null); // Placeholder, no action
        menuPanel.add(exitButton, MENUPANEL);

        cards.add(menuPanel, MENUPANEL);
        cards.add(gamePanel, GAMEPANEL);

        getContentPane().add(cards, BorderLayout.CENTER);
    }

    private void showBoardSizeOptions() {
        JFrame boardSizeFrame = new JFrame("Choose Board Size");
        boardSizeFrame.setSize(350, 400);
        boardSizeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        boardSizeFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 10, 10));

        String[] boardSizes = {"Extra Small", "Small", "Medium", "Large", "Extra Large"};
        for (String size : boardSizes) {
            JButton sizeButton = new JButton(size);
            sizeButton.addActionListener(e -> {
                selectBoard(size);
                boardSizeFrame.dispose();
            });
            panel.add(sizeButton);
        }

        boardSizeFrame.add(panel);
        boardSizeFrame.setVisible(true);
    }

    private void selectBoard(String size) {
        switch (size) {
            case "Extra Small":
                break;
            case "Small":
                break;
            case "Medium":
                break;
            case "Large":
                board = new LargeBoard();
                break;
            case "Extra Large":
                break;
            default:
                board = new LargeBoard();
                System.out.println("Invalid size, here should be exception");
        }
        runGame();
    }

    private void runGame(){
        EventQueue.invokeLater(() -> {

            var ex = new PacmanGame(board);
            ex.setVisible(true);
        });
    }

    private JButton createButton(String text, String card) {
        JButton button = new JButton(text);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (card != null) {
                    CardLayout cl = (CardLayout)(cards.getLayout());
                    cl.show(cards, card);
                } else if (text.equals("Exit")) {
                    System.exit(0);
                }
                // add more conditions as needed
            }
        });
        return button;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            PacmanMenu ex = new PacmanMenu();
            ex.setVisible(true);
        });}}