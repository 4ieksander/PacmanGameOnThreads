package pl.game.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class StatusPanel extends JPanel {
    private final int minHeight = 30;
    private int score;
    private long timeInSeconds;
    private int livesLeft;
    private Font font = new Font("Helvetica", Font.BOLD, 14);

    public StatusPanel() {
        setBackground(Color.BLACK);
        setForeground(Color.WHITE);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustLayout();
            }
        });
    }

    private void adjustLayout() {
        int height = getHeight();
        font = new Font("Helvetica", Font.BOLD, Math.max(10, height / 2));
    }
    @Override
    public Dimension getPreferredSize() {   // min height for panel
        return new Dimension(super.getPreferredSize().width, Math.max(minHeight, super.getPreferredSize().height));
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
        int width = getWidth();
        g.drawString("Score: " + score, width / 10, 20);  // Position at 10% of the width
        g.drawString("Time: " + formatTime(timeInSeconds), width * 4 / 10, 20);  // Position at 40% of the width
        g.drawString("Lives: " + livesLeft, width * 7 / 10, 20);  // Position at 70% of the width
    }

    private String formatTime(long seconds) {
        long mins = seconds / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d", mins, secs);

    }
}
