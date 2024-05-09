package pl.app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PacmanGame extends JFrame {

    private JPanel cards; // panel that uses CardLayout
    final static String MENUPANEL = "Menu Panel";
    final static String GAMEPANEL = "Game Panel";

    public PacmanGame() {
        setTitle("Pacman");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center the window
        initUI();
    }

    private void initUI() {
        cards = new JPanel(new CardLayout());

        JPanel menuPanel = new JPanel();
        menuPanel.add(createButton("New Game", GAMEPANEL));
        menuPanel.add(createButton("High Scores", null)); // Placeholder, no action
        menuPanel.add(createButton("Exit", null));

        JPanel gamePanel = new JPanel();
        gamePanel.setBackground(Color.BLACK);
        // Initialize your game panel here

        cards.add(menuPanel, MENUPANEL);
        cards.add(gamePanel, GAMEPANEL);

        getContentPane().add(cards, BorderLayout.CENTER);
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
            PacmanGame ex = new PacmanGame();
            ex.setVisible(true);
        });
    }
}