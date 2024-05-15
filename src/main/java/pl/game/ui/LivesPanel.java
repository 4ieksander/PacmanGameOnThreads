package pl.game.ui;

import javax.swing.*;
import java.awt.*;

public class LivesPanel extends JPanel {
    private int livesLeft;
    private ImageIcon lifeIcon;

    public LivesPanel() {
        setPreferredSize(new Dimension(150, 30));
        setBackground(Color.BLACK);
        loadLifeIcon();
    }

    private void loadLifeIcon() {
        Image image = new ImageIcon("src/main/resources/images/pacman/left/Left3.png").getImage();
        lifeIcon = new ImageIcon(image.getScaledInstance(20, 20, Image.SCALE_SMOOTH));
    }

    public void setLivesLeft(int lives) {
        this.livesLeft = lives;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < livesLeft; i++) {
            // Rysuj ikonę życia dla każdego życia
            lifeIcon.paintIcon(this, g, i * 28 + 8, 5);
        }
    }
}
