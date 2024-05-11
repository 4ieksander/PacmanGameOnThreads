package pl.app.game.subclasses;

import pl.app.game.GameEngine;

import javax.swing.*;
import java.awt.*;

public class GameRender {
    private GameEngine game;
    private ImageIcon ghost;
    private ImageIcon[] pacmanUp;
    private ImageIcon[] pacmanDown;
    private ImageIcon[] pacmanLeft;
    private ImageIcon[] pacmanRight;
    private final int PAC_ANIM_DELAY = 200;
    private final int PACMAN_IMAGES_COUNT = 4;
    private int currentImageNumber;
    private final Font smallFont = new Font("Helvetica", Font.BOLD, 14);
    private final Color dotColor = new Color(192, 192, 0);
    private Color mazeColor;

    public GameRender(GameEngine game) {
        this.game = game;
        this.loadAndScaleImages();
        mazeColor = new Color(5, 100, 5);
        startAnimation();

    }

    public void render(Graphics2D g){
        drawPacman(g);
        drawPowerUps(g);
        drawMaze(g);
        drawScore(g);
    }








    public void drawMaze(Graphics2D g) {
        int index = 0;
        for (int y = 0; y < game.SCREEN_SIZE; y += game.BLOCK_SIZE) {
            for (int x = 0; x < game.SCREEN_SIZE; x += game.BLOCK_SIZE) {
                g.setColor(mazeColor);
                g.setStroke(new BasicStroke(2));

                for (int bit = 1; bit <= 16; bit <<= 1) {
                    switch (bit) {
                        case 1:// Vertical line on the left
                            if ((game.getScreenData()[index] & bit) != 0)
                                g.drawLine(x, y, x, y + game.BLOCK_SIZE - 1);
                            break;
                        case 2: // Horizontal line at the top
                            if ((game.getScreenData()[index] & bit) != 0)
                                g.drawLine(x, y, x + game.BLOCK_SIZE - 1, y);
                            break;
                        case 4: // Vertical line on the right
                            if ((game.getScreenData()[index] & bit) != 0)
                                g.drawLine(x + game.BLOCK_SIZE - 1, y, x + game.BLOCK_SIZE - 1, y + game.BLOCK_SIZE - 1);
                            break;
                        case 8: // Horizontal line at the bottom
                            if ((game.getScreenData()[index] & bit) != 0)
                                g.drawLine(x, y + game.BLOCK_SIZE - 1, x + game.BLOCK_SIZE - 1, y + game.BLOCK_SIZE - 1);
                            break;
                        case 16: // Point to be collected
                            if ((game.getScreenData()[index] & bit) != 0) {
                                g.setColor(dotColor);
                                g.fillRect(x + 11, y + 11, 2, 2);
                            }
                            break;
                    }
                }
                index++;
            }
        }
    }


    public void drawGhost(Graphics2D g, int x, int y) {
        ghost.paintIcon(game, g, x, y);
    }

    public void drawPacman(Graphics2D g) {
        if (game.getViewDirectionX() == -1) {
            ImageIcon currentIcon = pacmanLeft[currentImageNumber];
            currentIcon.paintIcon(game, g, game.getPacmanPosX() - 1, game.getPacmanPosY());
        } else if (game.getViewDirectionX() == 1) {
            ImageIcon currentIcon = pacmanRight[currentImageNumber];
            currentIcon.paintIcon(game, g, game.getPacmanPosX() + 1, game.getPacmanPosY());
        } else if (game.getViewDirectionY() == -1) {
            ImageIcon currentIcon = pacmanUp[currentImageNumber];
            currentIcon.paintIcon(game, g, game.getPacmanPosX(), game.getPacmanPosY() - 1);
        } else {
            ImageIcon currentIcon = pacmanDown[currentImageNumber];
            currentIcon.paintIcon(game, g, game.getPacmanPosX(), game.getPacmanPosY() + 1);
        }}

        private void drawScore (Graphics2D g){

            int i;
            String s;

            g.setFont(smallFont);
            g.setColor(new Color(96, 128, 255));
            s = "Score: " + game.getScore();
            g.drawString(s, game.SCREEN_SIZE / 2 + 96, game.SCREEN_SIZE + 16);

            for (i = 0; i < game.getLivesLeft(); i++) {
                ghost.paintIcon(game, g, i * 28 + 8, game.SCREEN_SIZE + 1);
            }
        }



    public void showIntroScreen (Graphics2D g){
        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, game.SCREEN_SIZE / 2 - 30, game.SCREEN_SIZE - 100, 50);
        g.setColor(Color.white);
        g.drawRect(50, game.SCREEN_SIZE / 2 - 30, game.SCREEN_SIZE - 100, 50);

        String s = "Press enter or space to start.";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = game.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(s, (game.SCREEN_SIZE - metr.stringWidth(s)) / 2, game.SCREEN_SIZE/ 2);
    }


    private void startAnimation () {
            Thread animationThread = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        currentImageNumber = (currentImageNumber + 1) % PACMAN_IMAGES_COUNT;
                        Thread.sleep(PAC_ANIM_DELAY);
                        game.repaint();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
            animationThread.start();
        }

        private void loadAndScaleImages() {
            Image ghost_not_Scaled = new ImageIcon("src/main/resources/images/Ghost.png").getImage();
            ghost = new ImageIcon(ghost_not_Scaled.getScaledInstance(20, 20, Image.SCALE_SMOOTH));
            pacmanUp = loadAndScalePacmanForDirection("up/Up");
            pacmanDown = loadAndScalePacmanForDirection("down/Down");
            pacmanRight = loadAndScalePacmanForDirection("right/Right");
            pacmanLeft = loadAndScalePacmanForDirection("left/Left");
        }

        private ImageIcon[] loadAndScalePacmanForDirection(String directory_file){
            String pacmanIncompletePath = "src/main/resources/images/pacman/" + directory_file;
            Image pacmanImg1 = new ImageIcon(pacmanIncompletePath+"1.png").getImage();
            Image pacmanImg2 = new ImageIcon(pacmanIncompletePath+"2.png").getImage();
            Image pacmanImg3 = new ImageIcon(pacmanIncompletePath+"3.png").getImage();
            Image pacmanImg4 = new ImageIcon(pacmanIncompletePath+"4.png").getImage();
            return new ImageIcon[] {
                    new ImageIcon(pacmanImg1.getScaledInstance(25, 25, Image.SCALE_SMOOTH)),
                    new ImageIcon(pacmanImg2.getScaledInstance(25, 25, Image.SCALE_SMOOTH)),
                    new ImageIcon(pacmanImg3.getScaledInstance(25, 25, Image.SCALE_SMOOTH)),
                    new ImageIcon(pacmanImg4.getScaledInstance(25, 25, Image.SCALE_SMOOTH))
            };
        }

        private void drawPowerUps(Graphics2D g) {
            for (PowerUp powerUp : game.getActivePowerUps()) {
                if (powerUp.isActive()) {
                    g.setColor(Color.MAGENTA);
                    g.fillRect(powerUp.getPosX(), powerUp.getPosY(), game.BLOCK_SIZE, game.BLOCK_SIZE);
                }
            }
        }

}
