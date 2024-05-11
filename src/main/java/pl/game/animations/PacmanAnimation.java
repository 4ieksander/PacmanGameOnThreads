package pl.game.animations;

import javax.swing.*;
import java.awt.*;

public class PacmanAnimation extends JPanel {
    private ImageIcon[] images;
    private int currentImage = 0;

    public PacmanAnimation() {
        loadImages();
        setPreferredSize(new Dimension(50, 50)); // Ustaw rozmiar komponentu
        startAnimation();
    }

    private void loadImages() {
        String directoryPacmanPng = System.getProperty("user.dir") + "\\src\\main\\resources\\PNG\\Pacman\\";
        images = new ImageIcon[] {
                new ImageIcon(directoryPacmanPng + "right\\Right1.png"),
                new ImageIcon(directoryPacmanPng + "right\\Right2.png"),
                new ImageIcon(directoryPacmanPng + "right\\Right3.png"),
                new ImageIcon(directoryPacmanPng + "right\\Right4.png")
        };
    }

    private void startAnimation() {
        Thread animationThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    currentImage = (currentImage + 1) % images.length;
                    repaint();
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        animationThread.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ImageIcon currentIcon = images[currentImage];
        currentIcon.paintIcon(this, g, 0, 0); // Rysuj na (0,0) tego JPanel
    }
}
