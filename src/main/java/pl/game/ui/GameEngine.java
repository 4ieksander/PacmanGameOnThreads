package pl.game.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;
import javax.swing.*;

import pl.game.subclasses.PowerUp;

public class GameEngine extends JPanel implements ActionListener {
    public static final int BLOCK_SIZE = 24;

    private StatusPanel statusPanel;
    private final GameRender gameRender;
    private LivesPanel livesPanel;


    private final Random rand = new Random();
    private final short[] levelData;
    private final int SCREEN_SIZE;
    private final int[] validSpeeds = {1, 2, 3, 4, 5, 6};

    private final int DEFAULT_GHOST_START_X = 4;
    private final int DEFAULT_GHOST_START_Y = 4;

    private final int PACMAN_SPEED = 6;


    private boolean isInvulnerable = false;

    private Dimension dimension;

    private boolean inGame = false;
    private boolean dying = false;

    private int N_GHOSTS = 6;
    private int livesLeft, score;
    private int scoreMultipler;
    private int[] ghostMoveOptionX, ghostMoveOptionY;
    private int[] ghostPosX, ghostPosY, ghostDirX, ghostDirY, ghostSpeed;
    public int N_BLOCKS;

    private int pacmanPosX, pacmanPosY, PacmanDirX, PacmanDirY;
    private int tempDirX, tempDirY, viewDirectionX, viewDirectionY;

    private int currentSpeed = 3;
    private short[] screenData;
    List<PowerUp> activePowerUps = new ArrayList<>();

    private long startTime;
    private Thread timerThread;


    public GameEngine(short[] levelData, int N_BLOCKS) {
        this.N_BLOCKS = N_BLOCKS;
        this.levelData = levelData;
        this.SCREEN_SIZE = N_BLOCKS * BLOCK_SIZE;
        setLayout(new BorderLayout());
        statusPanel = new StatusPanel();
        livesPanel = new LivesPanel();
        gameRender = new GameRender(this);
        add(livesPanel, BorderLayout.SOUTH);
        add(statusPanel, BorderLayout.NORTH); // Position it at the top or wherever it fits best
        add(gameRender, BorderLayout.CENTER);

//        pack();
        initVariables();
        initBoard();
    }

    public void updateGameStatus(int score, long timeInSeconds, int livesLeft) {
        statusPanel.updateScore(score);
        statusPanel.updateTime(timeInSeconds);
        livesPanel.setLivesLeft(livesLeft);
        statusPanel.updateLives(livesLeft);
    }

    public void startGameTimer() {
        startTime = System.currentTimeMillis();
        timerThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                long elapsedSeconds = elapsedTime / 1000;
                long secondsDisplay = elapsedSeconds % 60;
                long elapsedMinutes = elapsedSeconds / 60;
                gameRender.setMinutes(elapsedMinutes);
                gameRender.setSeconds(secondsDisplay);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        timerThread.start();
    }

    public void stopGameTimer() {
        if (timerThread != null) {
            timerThread.interrupt();
        }
    }


    private void initBoard() {
        System.out.println("Board size set to: " + SCREEN_SIZE + "x" + SCREEN_SIZE);
        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.WHITE);
    }

    private void initVariables() {
        screenData = new short[N_BLOCKS * N_BLOCKS];
        System.out.println("Screen Data Length: " + screenData.length);
        System.out.println("Expected Length: " + (N_BLOCKS * N_BLOCKS));
        scoreMultipler = 1;
        dimension = new Dimension(SCREEN_SIZE+40, SCREEN_SIZE+40);
        int MAX_GHOSTS = 12;
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
        startGameTimer();
    }

    private void initLevel() {
        int i;
        for (i = 0; i < N_BLOCKS * N_BLOCKS; i++) {
            screenData[i] = levelData[i];
        }
        continueLevel();
    }

    private void startGameThread() {
        Thread gameThread = new Thread(() -> {
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
        g.setColor(Color.BLACK); // global background
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
            updateGameStatus(score, (gameRender.getMinutes()*60+gameRender.getSeconds()), livesLeft);
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
            stopGameTimer();
            inGame = false;
        }
        continueLevel();
    }

    private void continueLevel() {
        short i;
        int dx = 1;
        for (i = 0; i < N_GHOSTS; i++) {
            ghostPosY[i] = DEFAULT_GHOST_START_Y * BLOCK_SIZE;
            ghostPosX[i] = DEFAULT_GHOST_START_X * BLOCK_SIZE;
            ghostDirY[i] = 0;
            ghostDirX[i] = dx;
            dx = -dx;
        }
        resetGhostSpeed();
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
        for (int i = 0; i < N_GHOSTS; i++) {
            int nextX = ghostPosX[i] + ghostDirX[i] * ghostSpeed[i];
            int nextY = ghostPosY[i] + ghostDirY[i] * ghostSpeed[i];

            if (nextX < 0 || nextX >= SCREEN_SIZE || nextY < 0 || nextY >= SCREEN_SIZE) {
                resetGhostPosition(i);
            } else {
                int index = (nextX / BLOCK_SIZE) + N_BLOCKS * (nextY / BLOCK_SIZE);
                if (index >= 0 && index < screenData.length && (screenData[index] & 15) == 0) {
                    ghostPosX[i] = nextX;
                    ghostPosY[i] = nextY;
                } else {
                    chooseNewDirection(i);
                }
            }

            gameRender.drawGhost(g, ghostPosX[i] + 1, ghostPosY[i] + 1);

            if (!isInvulnerable && pacmanPosX > (ghostPosX[i] - 12) && pacmanPosX < (ghostPosX[i] + 12)
                    && pacmanPosY > (ghostPosY[i] - 12) && pacmanPosY < (ghostPosY[i] + 12) && inGame) {
                dying = true;
            }
        }
    }

    private void chooseNewDirection(int ghostIndex) {
        int count = 0;
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        for (int dir = 0; dir < dx.length; dir++) {
            int testX = ghostPosX[ghostIndex] + dx[dir] * BLOCK_SIZE;
            int testY = ghostPosY[ghostIndex] + dy[dir] * BLOCK_SIZE;

            int index = (testX / BLOCK_SIZE) + N_BLOCKS * (testY / BLOCK_SIZE);
            if (index >= 0 && index < screenData.length && (screenData[index] & 15) == 0) {
                ghostMoveOptionX[count] = dx[dir];
                ghostMoveOptionY[count] = dy[dir];
                count++;
            }
        }

        if (count > 0) {
            int chosenDirection = rand.nextInt(count);
            ghostDirX[ghostIndex] = ghostMoveOptionX[chosenDirection];
            ghostDirY[ghostIndex] = ghostMoveOptionY[chosenDirection];
        } else {
            ghostDirX[ghostIndex] = -ghostDirX[ghostIndex];
            ghostDirY[ghostIndex] = -ghostDirY[ghostIndex];
        }
    }


    private void resetGhostPosition(int ghostIndex) {
        ghostPosX[ghostIndex] = DEFAULT_GHOST_START_X * BLOCK_SIZE;
        ghostPosY[ghostIndex] = DEFAULT_GHOST_START_Y * BLOCK_SIZE;
        ghostDirX[ghostIndex] = 0;
        ghostDirY[ghostIndex] = 0;
        System.out.println("Resetting ghost " + ghostIndex + " to default position.");
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
                score += scoreMultipler;
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
        Iterator<PowerUp> iterator = activePowerUps.iterator();
        while (iterator.hasNext()) {
            PowerUp powerUp = iterator.next();
            if (powerUp.getPosX() == x && powerUp.getPosY() == y && powerUp.isActive()) {
                powerUp.activate();
                iterator.remove();
            }
        }
    }

    private boolean isValidLocation(int x, int y) {
        return screenData[y * N_BLOCKS + x] == 0 && activePowerUps.stream()
                .noneMatch(p -> p.getPosX() == x * BLOCK_SIZE && p.getPosY() == y * BLOCK_SIZE);
    }

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

    public void resetGhostSpeed() {
        for (int i = 0; i < N_GHOSTS; i++) {
            setGhostSpeed(i, getValidSpeeds()[rand.nextInt(getValidSpeeds().length)]);
        }
    }
    public int[] getGhostSpeed() {
        return ghostSpeed;
    }
    public void setGhostSpeed(int[] ghostSpeed) {
        this.ghostSpeed = ghostSpeed;
    }
    public void setGhostSpeed(int id, int ghostSpeed) {
        this.ghostSpeed[id] = ghostSpeed;
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
    public boolean isInvulnerable() {
        return isInvulnerable;
    }
    public int getPACMAN_SPEED(){
        return PACMAN_SPEED;
    }
    public int getScoreMultipler() {
        return scoreMultipler;
    }
    public void setScoreMultipler(int scoreMultipler) {
        this.scoreMultipler = scoreMultipler;
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
