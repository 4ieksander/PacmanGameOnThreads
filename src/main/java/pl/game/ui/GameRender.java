package pl.game.ui;

import pl.game.subclasses.PowerUp;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GameRender {
    private GameEngine game;
    private ImageIcon ghost;
    private ImageIcon pacmanIcon;
    private ImageIcon[] pacmanUpIcons;
    private ImageIcon[] pacmanDownIcons;
    private ImageIcon[] pacmanLeftIcons;
    private ImageIcon[] pacmanRightIcons;
    private Map<String, ImageIcon> powerUpIcons;
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
        for (int y = 0; y < game.getScreenSize(); y += game.BLOCK_SIZE) {
            for (int x = 0; x < game.getScreenSize(); x += game.BLOCK_SIZE) {
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
        float alpha = game.isInvulnerable() ? 0.5f : 1f;
        Composite originalComposite = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        ghost.paintIcon(game, g, x, y);
        g.setComposite(originalComposite);
    }


    public void drawPacman(Graphics2D g) {
        if (game.getViewDirectionX() == -1) {
            ImageIcon currentIcon = pacmanLeftIcons[currentImageNumber];
            currentIcon.paintIcon(game, g, game.getPacmanPosX() - 1, game.getPacmanPosY());
        } else if (game.getViewDirectionX() == 1) {
            ImageIcon currentIcon = pacmanRightIcons[currentImageNumber];
            currentIcon.paintIcon(game, g, game.getPacmanPosX() + 1, game.getPacmanPosY());
        } else if (game.getViewDirectionY() == -1) {
            ImageIcon currentIcon = pacmanUpIcons[currentImageNumber];
            currentIcon.paintIcon(game, g, game.getPacmanPosX(), game.getPacmanPosY() - 1);
        } else {
            ImageIcon currentIcon = pacmanDownIcons[currentImageNumber];
            currentIcon.paintIcon(game, g, game.getPacmanPosX(), game.getPacmanPosY() + 1);
        }}

    private void drawPowerUps(Graphics2D g) {
        for (PowerUp powerUp : game.getActivePowerUps()) {
            if (powerUp.isActive()) {
                ImageIcon icon = powerUpIcons.get(powerUp.type.name());
                if (icon != null) {
                    icon.paintIcon(game, g, powerUp.getPosX(), powerUp.getPosY());
                }
                else{
                    g.setColor(Color.MAGENTA);
                    g.fillRect(powerUp.getPosX(), powerUp.getPosY(), game.BLOCK_SIZE, game.BLOCK_SIZE);
                }
            }
        }
    }


    private void drawScore (Graphics2D g){

        int i;
        String s;

        g.setFont(smallFont);
        g.setColor(new Color(96, 128, 255));
        s = "Score: " + game.getScore();
        g.drawString(s, game.getScreenSize() / 2 + 96, game.getScreenSize() + 16);

        for (i = 0; i < game.getLivesLeft(); i++) {
            pacmanIcon.paintIcon(game, g, i * 28 + 8, game.getScreenSize() + 1);
        }
    }



    public void showIntroScreen (Graphics2D g){
        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, game.getScreenSize() / 2 - 30, game.getScreenSize() - 100, 50);

        String s = "Press enter or space to start.";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = game.getFontMetrics(small);

        g.setColor(Color.WHITE); // background for text
        g.setFont(small);
        g.drawString(s, (game.getScreenSize() - metr.stringWidth(s)) / 2, game.getScreenSize()/ 2);
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
        pacmanUpIcons = loadAndScalePacmanForDirection("up/Up");
        pacmanDownIcons = loadAndScalePacmanForDirection("down/Down");
        pacmanRightIcons = loadAndScalePacmanForDirection("right/Right");
        pacmanLeftIcons = loadAndScalePacmanForDirection("left/Left");
        pacmanIcon = pacmanLeftIcons[3];
        powerUpIcons = loadAndScaleBoosterIcons();
    }

    private ImageIcon[] loadAndScalePacmanForDirection(String directory_file){
        String pacmanIncompletePath = "src/main/resources/images/pacman/" + directory_file;
        return new ImageIcon[] {
                loadAndScaleIcon(pacmanIncompletePath+"1.png"),
                loadAndScaleIcon(pacmanIncompletePath+"2.png"),
                loadAndScaleIcon(pacmanIncompletePath+"3.png"),
                loadAndScaleIcon(pacmanIncompletePath+"4.png"),
        };
    }

    private Map<String, ImageIcon> loadAndScaleBoosterIcons(){
        Map<String, ImageIcon> boosterIcons = new HashMap<>();
        String boostersDirectory = "src/main/resources/images/boosters/";

        ImageIcon extraLife = loadAndScaleIcon(boostersDirectory+"ExtraLife.png");
        ImageIcon freeze = loadAndScaleIcon(boostersDirectory+"Freeze.png");
        ImageIcon invulnerability = loadAndScaleIcon(boostersDirectory+"Invulnerability.png");
        ImageIcon slowGhosts = loadAndScaleIcon(boostersDirectory+"SlowGhosts.png");
        ImageIcon scoreBooster = loadAndScaleIcon(boostersDirectory+"ScoreBooster.png");

        boosterIcons.put("EXTRA_LIFE", extraLife);
        boosterIcons.put("FREEZE", freeze);
        boosterIcons.put("INVULNERABILITY", invulnerability);
        boosterIcons.put("SLOW_GHOSTS", slowGhosts);
        boosterIcons.put("SCORE_BOOSTER", scoreBooster);
        return boosterIcons;
    }

    private ImageIcon loadAndScaleIcon(String path){
        Image image= new ImageIcon(path).getImage();
        return new ImageIcon(image.getScaledInstance(25, 25, Image.SCALE_SMOOTH));
    }
}
