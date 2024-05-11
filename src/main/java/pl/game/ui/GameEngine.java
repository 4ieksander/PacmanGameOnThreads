package pl.game.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import javax.swing.*;

import pl.game.subclasses.*;
import pl.game.subclasses.PowerUp;

public class GameEngine extends JPanel implements ActionListener {
    public static final int BLOCK_SIZE = 24;

    private GameRender gameRender;
    private Thread gameThread;
    private boolean isInvulnerable = false;

    private Dimension dimension;

    private Random rand = new Random();
    private boolean inGame = false;
    private boolean dying = false;

    private final int MAX_GHOSTS = 12;
    private final int PACMAN_SPEED = 6;

    private int N_GHOSTS = 6;
    private int livesLeft, score;
    private int[] ghostMoveOptionX, ghostMoveOptionY;
    private int[] ghostPosX, ghostPosY, ghostDirX, ghostDirY, ghostSpeed;
    private short[] levelData;
    public int N_BLOCKS;
    private int SCREEN_SIZE;

    private int pacmanPosX, pacmanPosY, PacmanDirX, PacmanDirY;
    private int tempDirX, tempDirY, viewDirectionX, viewDirectionY;


    private final int[] validSpeeds = {1, 2, 3, 4, 6, 8};
    private int currentSpeed = 3;
    private short[] screenData;
    List<PowerUp> activePowerUps = new ArrayList<>();

    private int DEFAULT_GHOST_START_X = 5;
    private int DEFAULT_GHOST_START_Y = 5;

    public GameEngine(short[] levelData, int N_BLOCKS) {
        this.N_BLOCKS = N_BLOCKS;
        this.levelData = levelData;
         this.SCREEN_SIZE = N_BLOCKS * BLOCK_SIZE;
        gameRender = new GameRender(this);
        initVariables();
        initBoard();
    }


    private void initBoard() {
        setBackground(Color.black); // Ensure background is visible
        System.out.println("Board size set to: " + SCREEN_SIZE + "x" + SCREEN_SIZE);
        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.black);
    }

    private void initVariables() {
        screenData = new short[N_BLOCKS * N_BLOCKS];
        System.out.println("Screen Data Length: " + screenData.length);
        System.out.println("Expected Length: " + (N_BLOCKS * N_BLOCKS));
        dimension = new Dimension(SCREEN_SIZE+40, SCREEN_SIZE+40);
        ghostPosX = new int[MAX_GHOSTS];
        ghostDirX = new int[MAX_GHOSTS];
        ghostPosY = new int[MAX_GHOSTS];
        ghostDirY = new int[MAX_GHOSTS];
        ghostSpeed = new int[MAX_GHOSTS];
        ghostMoveOptionX = new int[4];
        ghostMoveOptionY = new int[4];

        startGameThread();
        startPowerUpGenerator();
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

    private void startGameThread() {
        gameThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    SwingUtilities.invokeLater(this::repaint);
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        gameThread.start();
    }


    private void doDrawing(Graphics g) {
        Graphics2D graphics = (Graphics2D) g;
        g.setColor(Color.black);
        g.fillRect(0, 0, dimension.width, dimension.height);
        gameRender.render(graphics);
        if (inGame) {
            playGame(graphics);
        } else {
            gameRender.showIntroScreen(graphics);
        }
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    private void playGame(Graphics2D g) {
        if (dying) {
            death();
        } else {
            movePacman();
            moveGhosts(g);
            gameRender.render(g);
            checkMaze();
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
//            initLevel();
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
        pacmanPosY = 10 * BLOCK_SIZE;
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

                if (pos < 0 || pos > N_BLOCKS*N_BLOCKS-1){
                    System.out.println("POS" + pos);
                    System.out.println("I" + i);
                    resetGhostPosition(i);
                    continue;
                }
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
            gameRender.drawGhost(g, ghostPosX[i] + 1, ghostPosY[i] + 1);

            if (pacmanPosX > (ghostPosX[i] - 12) && pacmanPosX < (ghostPosX[i] + 12)
                    && pacmanPosY > (ghostPosY[i] - 12) && pacmanPosY < (ghostPosY[i] + 12)
                    && inGame) {

                dying = true;
            }
        }
    }

    private void resetGhostPosition(int ghostIndex) {
        // Możesz także wybrać losową bezpieczną lokalizację z dostępnych
        ghostPosX[ghostIndex] = DEFAULT_GHOST_START_X;
        ghostPosY[ghostIndex] = DEFAULT_GHOST_START_Y;
        ghostDirX[ghostIndex] = 0; // Zatrzymaj ruch duszka w kierunku X
        ghostDirY[ghostIndex] = 0; // Zatrzymaj ruch duszka w kierunku Y
    }

    private boolean canMove(int x, int y) {
        if (x < 0 || y < 0 || x >= N_BLOCKS * BLOCK_SIZE || y >= N_BLOCKS * BLOCK_SIZE) {
            return false;
        }
        int index = x / BLOCK_SIZE + N_BLOCKS * (y / BLOCK_SIZE);
        if (index < 0 || index >= screenData.length) {
            return false;
        }
        return screenData[index] == 0;
    }


    private void selectNewDirection(int ghostIndex) {
        int[] possibleDirections = new int[]{-1, 0, 1};
        boolean moved = false;

        while (!moved) {
            int dirX = possibleDirections[(int) (Math.random() * 3)];
            int dirY = possibleDirections[(int) (Math.random() * 3)];
            if (dirX != 0 && dirY != 0) {
                continue;
            }

            int nextX = ghostPosX[ghostIndex] + dirX * BLOCK_SIZE;
            int nextY = ghostPosY[ghostIndex] + dirY * BLOCK_SIZE;
            if (canMove(nextX, nextY)) {
                ghostDirX[ghostIndex] = dirX;
                ghostDirY[ghostIndex] = dirY;
                moved = true;
            }
        }
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

    // Boosters
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

    private boolean isValidLocation(int x, int y) {
        return screenData[y * N_BLOCKS + x] == 0 && activePowerUps.stream()
                .noneMatch(p -> p.getPosX() == x * BLOCK_SIZE && p.getPosY() == y * BLOCK_SIZE);
    }
//    private boolean isValidLocation(int x, int y) {
//
//        if (x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT) {
//            return false;
//        }
//        int index = x / BLOCK_SIZE + N_BLOCKS * (y / BLOCK_SIZE);
//
//        if (index < 0 || index >= screenData.length) {
//            resetEnemyPosition();
//            return false;
//        }
//        else
//        {
//        return screenData[index] == 0;
//    }}
//    private void resetEnemyPosition() {
//        // Zakładamy, że masz zdefiniowane domyślne współrzędne startowe dla wroga
//        enemyX = DEFAULT_ENEMY_START_X;
//        enemyY = DEFAULT_ENEMY_START_Y;
//    }


    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        initGame();
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
    public void setCurrentSpeed(int currentSpeed) {
        this.currentSpeed = currentSpeed;
    }
    public void incresePacmanLifes(){
        this.livesLeft++;
    }
    public void setInvulnerable(Boolean invulnerable){
        this.isInvulnerable = invulnerable;
    }
    public int[] getValidSpeeds() {
        return validSpeeds;
    }
    public short[] getScreenData(){
        return screenData;
    }
    public List<PowerUp> getActivePowerUps() {
        return activePowerUps;
    }
    public int getViewDirectionX(){
        return viewDirectionX;
    }
    public int getViewDirectionY(){
        return viewDirectionY;
    }
    public int getPacmanPosX(){
        return pacmanPosX;
    }
    public int getPacmanPosY(){
        return pacmanPosY;
    }
    public int getScore() {
        return score;
    }
    public int getLivesLeft(){
        return livesLeft;
    }
    public int getScreenSize(){
        return SCREEN_SIZE;
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
}
