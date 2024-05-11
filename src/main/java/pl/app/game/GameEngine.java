package pl.app.game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import javax.swing.*;

import pl.app.game.subclasses.*;

public class GameEngine extends JPanel implements ActionListener {

    private Thread gameThread;
    private boolean isInvulnerable = false;

    private Dimension d;
    private final Font smallFont = new Font("Helvetica", Font.BOLD, 14);

    private final Color dotColor = new Color(192, 192, 0);
    private Color mazeColor;
    private Random rand = new Random();
    private boolean inGame = false;
    private boolean dying = false;

    private final int BLOCK_SIZE = 24;
    private final int N_BLOCKS = 15;
    private final int SCREEN_SIZE = N_BLOCKS * BLOCK_SIZE;
    private final int PAC_ANIM_DELAY = 200;
    private final int PACMAN_IMAGES_COUNT = 4;
    private final int MAX_GHOSTS = 12;
    private final int PACMAN_SPEED = 6;

    private int N_GHOSTS = 6;
    private int livesLeft, score;
    private int[] ghostMoveOptionX, ghostMoveOptionY;
    private int[] ghostPosX, ghostPosY, ghostDirX, ghostDirY, ghostSpeed;

    private int currentImageNumber = 0;

    private ImageIcon ghost;
    private ImageIcon[] pacmanUp;
    private ImageIcon[] pacmanDown;
    private ImageIcon[] pacmanLeft;
    private ImageIcon[] pacmanRight;

    private int pacmanPosX, pacmanPosY, PacmanDirX, PacmanDirY;
    private int tempDirX, tempDirY, viewDirectionX, viewDirectionY;

    // This map is the representation of 5 binary bits in the decimal system
    // binary: 0, 0, 0, 0, 0 => point present, (walls) boottom, right, up, left
    // ex. 26 (decimal) -> 1 1 0 1 0 (binary) -> point exists, bottom and up
    private final short[] levelData = {
            19, 26, 26, 26, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,
            21, 00, 00, 00, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            21, 00, 00, 00, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            21, 00, 00, 00, 17, 16, 16, 24, 16, 16, 16, 16, 16, 16, 20,
            17, 18, 18, 18, 16, 16, 20, 00, 17, 16, 16, 16, 16, 16, 20,
            17, 16, 16, 16, 16, 16, 20, 00, 17, 16, 16, 16, 16, 24, 20,
            25, 16, 16, 16, 24, 24, 28, 00, 25, 24, 24, 16, 20, 00, 21,
            01, 17, 16, 20, 00, 00, 00, 00, 00, 00, 00, 17, 20, 00, 21,
            01, 17, 16, 16, 18, 18, 22, 00, 19, 18, 18, 16, 20, 00, 21,
            01, 17, 16, 16, 16, 16, 20, 00, 17, 16, 16, 16, 20, 00, 21,
            01, 17, 16, 16, 16, 16, 20, 00, 17, 16, 16, 16, 20, 00, 21,
            01, 17, 16, 16, 16, 16, 16, 18, 16, 16, 16, 16, 20, 00, 21,
            01, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20, 00, 21,
            01, 25, 24, 24, 24, 24, 24, 24, 24, 24, 16, 16, 16, 18, 20,
            9,8, 8, 8, 8, 8, 8, 8, 8, 8, 25, 24, 24, 24, 28
    };

    private final int[] validSpeeds = {1, 2, 3, 4, 6, 8};
    private final int maxSpeed = 6;

    private int currentSpeed = 3;
    private short[] screenData;
    List<PowerUp> activePowerUps = new ArrayList<>();



    // Threads
    private void startAnimation() {
        Thread animationThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    currentImageNumber = (currentImageNumber + 1) % PACMAN_IMAGES_COUNT;
                    Thread.sleep(PAC_ANIM_DELAY);
                    repaint();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        animationThread.start();
    }




    public void boostSpeedPermamently() {
        if (currentSpeed < maxSpeed) {
            currentSpeed++;
        }
    }

    public void boostSpeedTemporarily(){
        currentSpeed = currentSpeed + 5;
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        currentSpeed = currentSpeed - 5;
    }

    public void freezeGhosts() {
        int[] originalSpeeds = new int[MAX_GHOSTS];
        System.arraycopy(ghostSpeed, 0, originalSpeeds, 0, ghostSpeed.length);
        Arrays.fill(ghostSpeed, 0); // Freezes all ghosts.

        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            System.arraycopy(originalSpeeds, 0, ghostSpeed, 0, ghostSpeed.length);
        }
    }

    public GameEngine() {
        loadAndScaleImages();
        initVariables();
        initBoard();
    }

    private void initBoard() {

        addKeyListener(new TAdapter());

        setFocusable(true);

        setBackground(Color.black);
    }

    private void initVariables() {
        screenData = new short[N_BLOCKS * N_BLOCKS];
        mazeColor = new Color(5, 100, 5);
        d = new Dimension(400, 400);
        ghostPosX = new int[MAX_GHOSTS];
        ghostDirX = new int[MAX_GHOSTS];
        ghostPosY = new int[MAX_GHOSTS];
        ghostDirY = new int[MAX_GHOSTS];
        ghostSpeed = new int[MAX_GHOSTS];
        ghostMoveOptionX = new int[4];
        ghostMoveOptionY = new int[4];

        startGameThread();
        startAnimation();
        startPowerUpGenerator();
    }
    private void startGameThread() {
        gameThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    SwingUtilities.invokeLater(() -> repaint());
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        gameThread.start();
    }
    private void startPowerUpGenerator() {
        Thread powerUpGenerator = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(500);
                    for (int i = 0; i < N_GHOSTS; i++) {
                        if (rand.nextInt(4) == 0) {
                            int posX = ghostPosX[i] / BLOCK_SIZE;
                            int posY = ghostPosY[i] / BLOCK_SIZE;
                            if (isValidLocation(posX, posY)) {
                                createPowerUp(posX, posY);
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        powerUpGenerator.start();
    }


    private void createPowerUp(int posX, int posY) {
        PowerUp.Type type = PowerUp.Type.values()[rand.nextInt(PowerUp.Type.values().length)];
        if (isValidLocation(posX, posY)) { // second checking
            PowerUp newPowerUp = new PowerUp(type, posX * BLOCK_SIZE, posY * BLOCK_SIZE, this);
            SwingUtilities.invokeLater(() -> {
                activePowerUps.add(newPowerUp);
                repaint();
            });
        }
    }

    private void drawPowerUps(Graphics2D g) {
        for (PowerUp powerUp : activePowerUps) {
            if (powerUp.isActive()) {
                g.setColor(Color.MAGENTA);
                g.fillRect(powerUp.getPosX(), powerUp.getPosY(), BLOCK_SIZE, BLOCK_SIZE);
            }
        }
    }


    private boolean isValidLocation(int x, int y) {
        return screenData[y * N_BLOCKS + x] == 0 && activePowerUps.stream()
                .noneMatch(p -> p.getPosX() == x * BLOCK_SIZE && p.getPosY() == y * BLOCK_SIZE);
    }


    @Override
    public void addNotify() {
        super.addNotify();

        initGame();
    }

    private void playGame(Graphics2D g) {

        if (dying) {

            death();

        } else {

            movePacman();
            drawPacman(g);
            moveGhosts(g);
            drawPowerUps(g);
            checkMaze();
        }
    }

    private void showIntroScreen(Graphics2D g) {

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, SCREEN_SIZE / 2 - 30, SCREEN_SIZE - 100, 50);
        g.setColor(Color.white);
        g.drawRect(50, SCREEN_SIZE / 2 - 30, SCREEN_SIZE - 100, 50);

        String s = "Press enter or space to start.";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(s, (SCREEN_SIZE - metr.stringWidth(s)) / 2, SCREEN_SIZE / 2);
    }

    private void drawScore(Graphics2D g) {

        int i;
        String s;

        g.setFont(smallFont);
        g.setColor(new Color(96, 128, 255));
        s = "Score: " + score;
        g.drawString(s, SCREEN_SIZE / 2 + 96, SCREEN_SIZE + 16);

        for (i = 0; i < livesLeft; i++) {
//            g.drawImage(ghost, i * 28 + 8, SCREEN_SIZE + 1, this);
            ghost.paintIcon(this, g, i * 28 + 8, SCREEN_SIZE +1);
        }
    }

    private void checkMaze() {

        short i = 0;
        boolean finished = true;

        while (i < N_BLOCKS * N_BLOCKS && finished) {

            if ((screenData[i] & 48) != 0) {
                finished = false;
            }

            i++;
        }

        if (finished) {

            score += 50;

            if (N_GHOSTS < MAX_GHOSTS) {
                N_GHOSTS++;
            }



            initLevel();
        }
    }

    private void death() {

        livesLeft--;

        if (livesLeft == 0) {
            inGame = false;
        }

        continueLevel();
    }

        private void continueLevel() {

        short i;
        int dx = 1;
        int random;

        for (i = 0; i < N_GHOSTS; i++) {

            ghostPosY[i] = 4 * BLOCK_SIZE;
            ghostPosX[i] = 4 * BLOCK_SIZE;
            ghostDirY[i] = 0;
            ghostDirX[i] = dx;
            dx = -dx;
            random = (int) (Math.random() * (currentSpeed + 1));

            if (random > currentSpeed) {
                random = currentSpeed;
            }

            ghostSpeed[i] = validSpeeds[random];
        }

        pacmanPosX = 7 * BLOCK_SIZE;
        pacmanPosY = 11 * BLOCK_SIZE;
        PacmanDirX = 0;
        PacmanDirY = 0;
        tempDirX = 0;
        tempDirY = 0;
        viewDirectionX = -1;
        viewDirectionY = 0;
        dying = false;
    }

    private void moveGhosts(Graphics2D g) {

        short i;
        int pos;
        int count;

        for (i = 0; i < N_GHOSTS; i++) {
            if (ghostPosX[i] % BLOCK_SIZE == 0 && ghostPosY[i] % BLOCK_SIZE == 0) {
                pos = ghostPosX[i] / BLOCK_SIZE + N_BLOCKS * (int) (ghostPosY[i] / BLOCK_SIZE);

                count = 0;

                if ((screenData[pos] & 1) == 0 && ghostDirX[i] != 1) {
                    ghostMoveOptionX[count] = -1;
                    ghostMoveOptionY[count] = 0;
                    count++;
                }

                if ((screenData[pos] & 2) == 0 && ghostDirY[i] != 1) {
                    ghostMoveOptionX[count] = 0;
                    ghostMoveOptionY[count] = -1;
                    count++;
                }

                if ((screenData[pos] & 4) == 0 && ghostDirX[i] != -1) {
                    ghostMoveOptionX[count] = 1;
                    ghostMoveOptionY[count] = 0;
                    count++;
                }

                if ((screenData[pos] & 8) == 0 && ghostDirY[i] != -1) {
                    ghostMoveOptionX[count] = 0;
                    ghostMoveOptionY[count] = 1;
                    count++;
                }

                if (count == 0) {

                    if ((screenData[pos] & 15) == 15) {
                        ghostDirX[i] = 0;
                        ghostDirY[i] = 0;
                    } else {
                        ghostDirX[i] = -ghostDirX[i];
                        ghostDirY[i] = -ghostDirY[i];
                    }

                } else {

                    count = (int) (Math.random() * count);

                    if (count > 3) {
                        count = 3;
                    }

                    ghostDirX[i] = ghostMoveOptionX[count];
                    ghostDirY[i] = ghostMoveOptionY[count];
                }

            }

            ghostPosX[i] = ghostPosX[i] + (ghostDirX[i] * ghostSpeed[i]);
            ghostPosY[i] = ghostPosY[i] + (ghostDirY[i] * ghostSpeed[i]);
            drawGhost(g, ghostPosX[i] + 1, ghostPosY[i] + 1);

            if (pacmanPosX > (ghostPosX[i] - 12) && pacmanPosX < (ghostPosX[i] + 12)
                    && pacmanPosY > (ghostPosY[i] - 12) && pacmanPosY < (ghostPosY[i] + 12)
                    && inGame) {

                dying = true;
            }
        }
    }

    private void drawGhost(Graphics2D g, int x, int y) {
        ghost.paintIcon(this, g, x, y);
    }

    private void movePacman() {

        int pos;
        short ch;

        if (tempDirX == -PacmanDirX && tempDirY == -PacmanDirY) {
            PacmanDirX = tempDirX;
            PacmanDirY = tempDirY;
            viewDirectionX = PacmanDirX;
            viewDirectionY = PacmanDirY;
        }

        if (pacmanPosX % BLOCK_SIZE == 0 && pacmanPosY % BLOCK_SIZE == 0) {
            pos = pacmanPosX / BLOCK_SIZE + N_BLOCKS * (int) (pacmanPosY / BLOCK_SIZE);
            ch = screenData[pos];

            checkForPowerUps(pacmanPosX, pacmanPosY);

            if ((ch & 16) != 0) {
                screenData[pos] = (short) (ch & 15);
                score++;
            }

            if (tempDirX != 0 || tempDirY != 0) {
                if (!((tempDirX == -1 && tempDirY == 0 && (ch & 1) != 0)
                        || (tempDirX == 1 && tempDirY == 0 && (ch & 4) != 0)
                        || (tempDirX == 0 && tempDirY == -1 && (ch & 2) != 0)
                        || (tempDirX == 0 && tempDirY == 1 && (ch & 8) != 0))) {
                    PacmanDirX = tempDirX;
                    PacmanDirY = tempDirY;
                    viewDirectionX = PacmanDirX;
                    viewDirectionY = PacmanDirY;
                }
            }

            // Check for standstill
            if ((PacmanDirX == -1 && PacmanDirY == 0 && (ch & 1) != 0)
                    || (PacmanDirX == 1 && PacmanDirY == 0 && (ch & 4) != 0)
                    || (PacmanDirX == 0 && PacmanDirY == -1 && (ch & 2) != 0)
                    || (PacmanDirX == 0 && PacmanDirY == 1 && (ch & 8) != 0)) {
                PacmanDirX = 0;
                PacmanDirY = 0;
            }
        }
        pacmanPosX = pacmanPosX + PACMAN_SPEED * PacmanDirX;
        pacmanPosY = pacmanPosY + PACMAN_SPEED * PacmanDirY;
    }

    private void checkForPowerUps(int x, int y) {
        List<PowerUp> collected = new ArrayList<>();
        for (PowerUp powerUp : activePowerUps) {
            if (powerUp.getPosX() == x && powerUp.getPosY() == y && powerUp.isActive()) {
                powerUp.activate();
                collected.add(powerUp);
            }
        }
        activePowerUps.removeAll(collected);
    }


    private void drawPacman(Graphics2D g) {
        if (viewDirectionX == -1) {
            ImageIcon currentIcon = pacmanLeft[currentImageNumber];
            currentIcon.paintIcon(this, g, pacmanPosX -1, pacmanPosY);
        } else if (viewDirectionX == 1) {
            ImageIcon currentIcon = pacmanRight[currentImageNumber];
            currentIcon.paintIcon(this, g, pacmanPosX +1, pacmanPosY);
        } else if (viewDirectionY == -1) {
            ImageIcon currentIcon = pacmanUp[currentImageNumber];
            currentIcon.paintIcon(this, g, pacmanPosX, pacmanPosY -1);
        } else {
            ImageIcon currentIcon = pacmanDown[currentImageNumber];
            currentIcon.paintIcon(this, g, pacmanPosX, pacmanPosY +1);
        }
    }





    private void drawMaze(Graphics2D g) {
        int index = 0;
        for (int y = 0; y < SCREEN_SIZE; y += BLOCK_SIZE) {
            for (int x = 0; x < SCREEN_SIZE; x += BLOCK_SIZE) {
                g.setColor(mazeColor);
                g.setStroke(new BasicStroke(2));

                for (int bit = 1; bit <= 16; bit <<= 1) {
                    switch (bit) {
                        case 1:// Vertical line on the left
                            if ((screenData[index] & bit) != 0)
                                g.drawLine(x, y, x, y + BLOCK_SIZE - 1);
                            break;
                        case 2: // Horizontal line at the top
                            if ((screenData[index] & bit) != 0)
                                g.drawLine(x, y, x + BLOCK_SIZE - 1, y);
                            break;
                        case 4: // Vertical line on the right
                            if ((screenData[index] & bit) != 0)
                                g.drawLine(x + BLOCK_SIZE - 1, y, x + BLOCK_SIZE - 1, y + BLOCK_SIZE - 1);
                            break;
                        case 8: // Horizontal line at the bottom
                            if ((screenData[index] & bit) != 0)
                                g.drawLine(x, y + BLOCK_SIZE - 1, x + BLOCK_SIZE - 1, y + BLOCK_SIZE - 1);
                            break;
                        case 16: // Point to be collected
                            if ((screenData[index] & bit) != 0) {
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

    private void initGame() {
        livesLeft = 3;
        score = 0;
        initLevel();
        N_GHOSTS = 6;
        currentSpeed = 3;
    }

    private void initLevel() {
        int i;
        for (i = 0; i < N_BLOCKS * N_BLOCKS; i++) {
            screenData[i] = levelData[i];
        }
        continueLevel();
    }


    private void doDrawing(Graphics g) {
        Graphics2D graphics = (Graphics2D) g;
        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);

        drawMaze(graphics);
        drawScore(graphics);

        if (inGame) {
            playGame(graphics);
        } else {
            showIntroScreen(graphics);
        }

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
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

    public int[] getGhostSpeed() {
        return ghostSpeed;
    }


    public void setGhostSpeed(int[] ghostSpeed) {
        this.ghostSpeed = ghostSpeed;
    }
    public void setGhostSpeed(int id, int ghostSpeed) {
        this.ghostSpeed[id] = id;
    }

    public int getCurrentSpeed(){
        return currentSpeed;
    }
    public void setCurrentSpeed(int currentSpeed) {this.currentSpeed = currentSpeed;}
    public void incresePacmanLifes(){
        this.livesLeft++;
    }
    public void setInvulnerable(Boolean invulnerable){
        this.isInvulnerable = invulnerable;
    }

    public int[] getValidSpeeds() {
        return validSpeeds;
    }

    class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if (inGame) {
                switch (key) {
                    case KeyEvent.VK_LEFT:
                        tempDirX = -1;
                        tempDirY = 0;
                        break;
                    case KeyEvent.VK_RIGHT:
                        tempDirX = 1;
                        tempDirY = 0;
                        break;
                    case KeyEvent.VK_UP:
                        tempDirX = 0;
                        tempDirY = -1;
                        break;
                    case KeyEvent.VK_DOWN:
                        tempDirX = 0;
                        tempDirY = 1;
                        break;
                    default:
                        break;
                }
            } else {
                if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_ENTER) {
                    inGame = true;
                    initGame();
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

            int key = e.getKeyCode();

            if (key == Event.LEFT || key == Event.RIGHT
                    || key == Event.UP || key == Event.DOWN) {
                tempDirX = 0;
                tempDirY = 0;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
