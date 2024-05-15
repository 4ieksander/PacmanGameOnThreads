package pl.game.subclasses;

import javax.swing.*;
import java.awt.*;

public class StatusPanel extends JPanel {
    private int score;
    private long timeInSeconds;
    private int livesLeft;
    private Font font = new Font("Helvetica", Font.BOLD, 14);

    public StatusPanel() {
        setPreferredSize(new Dimension(400, 30));
        setBackground(Color.BLACK);
        setForeground(Color.WHITE);
    }

    public void updateScore(int score) {
        this.score = score;
        repaint();
    }

    public void updateTime(long timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
        repaint();
    }

    public void updateLives(int livesLeft) {
        this.livesLeft = livesLeft;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 20, 20);
        g.drawString("Time: " + formatTime(timeInSeconds), 200, 20);
        g.drawString("Lives: " + livesLeft, 400, 20);
    }

    private String formatTime(long seconds) {
        long mins = seconds / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d", mins, secs);
    }
}

