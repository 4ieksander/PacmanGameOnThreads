package pl.app.animations;

import javax.swing.*;
import java.awt.*;

public class PacmanAnimation extends JFrame {
    private JLabel label;
    private ImageIcon openMouth;
    private ImageIcon halfOpenMouth;
    private ImageIcon halfClosedMouth;
    private ImageIcon closedMouth;
    private ImageIcon[] images;
    private int currentImage = 0;

    public PacmanAnimation() {
        setTitle("Pacman Animation");
        setSize(300, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
        startAnimation();
    }

    private void initUI() {
        // Załaduj obrazy
        String diractoryPacmanPng = System.getProperty("user.dir")+ "\\src\\main\\resources\\PNG\\Pacman\\";
        closedMouth = new ImageIcon(diractoryPacmanPng+"Left1.png");
        halfClosedMouth = new ImageIcon(diractoryPacmanPng+"Left2.png");
        halfOpenMouth = new ImageIcon(diractoryPacmanPng+"Left3.png");
        openMouth = new ImageIcon(diractoryPacmanPng+"Left4.png");

        images = new ImageIcon[] {openMouth, halfOpenMouth, halfClosedMouth, closedMouth, halfClosedMouth, halfOpenMouth};

        label = new JLabel(images[0]);
        add(label);
    }

    private void startAnimation() {
        Thread animationThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    // Zmiana obrazu
                    currentImage = (currentImage + 1) % images.length;
                    SwingUtilities.invokeLater(() -> {
                        label.setIcon(images[currentImage]);
                    });

                    // Czas pomiędzy zmianami grafik
                    Thread.sleep(200); // 200 ms pomiędzy zmianami obrazu
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        animationThread.start();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            new PacmanAnimation().setVisible(true);
        });
    }
}