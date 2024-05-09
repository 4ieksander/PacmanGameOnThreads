package pl.app;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import pl.app.animations.*;


public class PacmanGame extends JFrame {

    private JLabel scoreLabel, timeLabel, livesLabel;
    private int score = 0, lives = 3, time = 0;
    private Thread timeThread, scoreThread, livesThread;
    private String boardSize;

    public PacmanGame(String boardSize) {
        boardSize = boardSize;
        setTitle("Pacman");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        initUI();
        initThreads();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        JPanel infoPanel = new JPanel(new GridLayout(1, 3));

        scoreLabel = new JLabel("Score: " + score);
        timeLabel = new JLabel("Time: " + time);
        livesLabel = new JLabel("Lives: " + lives);

        infoPanel.add(scoreLabel);
        infoPanel.add(timeLabel);
        infoPanel.add(livesLabel);
        setLayout(new BorderLayout());
        PacmanAnimation pacmanAnimation = new PacmanAnimation();
        add(pacmanAnimation, BorderLayout.CENTER); //

        add(infoPanel, BorderLayout.NORTH);


    }

    private void initThreads() {
        timeThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(1000);
                    SwingUtilities.invokeLater(() -> {
                        time++;
                        timeLabel.setText("Time: " + time);
                    });
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        scoreThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(100); // Możesz dostosować częstotliwość aktualizacji punktów
                    SwingUtilities.invokeLater(() -> {
                        scoreLabel.setText("Score: " + score);
                    });
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        livesThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(500); // Możesz dostosować, kiedy to aktualizować
                    SwingUtilities.invokeLater(() -> {
                        livesLabel.setText("Lives: " + lives);
                    });
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        timeThread.start();
        scoreThread.start();
        livesThread.start();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            PacmanGame ex = new PacmanGame("Small");
            ex.setVisible(true);
        });
    }
}
