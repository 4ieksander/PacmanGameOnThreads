package pl.app.ui;

import pl.app.models.Board;
import pl.app.models.Pacman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel implements KeyListener {
    private Board board;
    private Pacman pacman;
    private final int tileWidth = 20;  // szerokość kafelka
    private final int tileHeight = 20; // wysokość kafelka

    public GamePanel() {
        this.board = new Board();
        this.pacman = new Pacman();
        setPreferredSize(new Dimension(400, 450)); // Rozmiar okna gry
        setFocusable(true);
        addKeyListener(this);
        // Inne inicjalizacje
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getCols(); col++) {
                int x = col * tileWidth;
                int y = row * tileHeight;
                if (board.getTile(row, col) == 1) {
                    g.setColor(Color.BLUE); // Kolor ścian
                    g.fillRect(x, y, tileWidth, tileHeight);
                } else if (board.getTile(row, col) == 2) {
                    g.setColor(Color.YELLOW); // Kolor punktów
                    g.fillOval(x + tileWidth / 4, y + tileHeight / 4, tileWidth / 2, tileHeight / 2);
                }
            }
        }
        // Rysowanie Pacmana
        g.setColor(Color.YELLOW);
        g.fillArc(pacman.getX(), pacman.getY(), tileWidth, tileHeight, 0, 360);
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_LEFT:
                pacman.move(-20, 0, board); // zakładając, że 20 to rozmiar jednego kafelka
                break;
            case KeyEvent.VK_RIGHT:
                pacman.move(20, 0, board);
                break;
            case KeyEvent.VK_UP:
                pacman.move(0, -20, board);
                break;
            case KeyEvent.VK_DOWN:
                pacman.move(0, 20, board);
                break;
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
