package pl.game.ui;

import javax.swing.*;
import java.awt.*;

public class GameInterface extends JFrame {
    private GameEngine gameEngine;
    private StatusPanel statusPanel;
    private LivesPanel livesPanel;

    public GameInterface() {
        setTitle("Pacman Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(800, 600);

//        gameEngine = new GameEngine();
        statusPanel = new StatusPanel();
        livesPanel = new LivesPanel(); // Assuming 3 lives at start

        add(statusPanel, BorderLayout.NORTH);
        add(gameEngine, BorderLayout.CENTER);
        add(livesPanel, BorderLayout.SOUTH);

        // Start the game timer and update the game status regularly
//        gameEngine.startGameTimer();
        setupStatusUpdater();

        setLocationRelativeTo(null); // Center on screen
        setVisible(true);
    }

    private void setupStatusUpdater() {
        Timer timer = new Timer(1000, e -> {
            statusPanel.updateScore(gameEngine.getScore());
//            statusPanel.updateTime(gameEngine.getSecondsElapsed());
            statusPanel.updateLives(gameEngine.getLivesLeft());
            livesPanel.setLivesLeft(gameEngine.getLivesLeft());
        });
        timer.start();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(GameInterface::new);
    }
}
