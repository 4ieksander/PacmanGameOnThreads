package pl.game.subclasses;

import java.util.Random;

public class PowerUp {
    public enum Type {
        SCORE_BOOSTER, FREEZE, EXTRA_LIFE, INVULNERABILITY, SLOW_GHOSTS
    }

    public Type type;
    int posX;
    int posY;
    boolean isActive;
    private GameEngine game;
    private Random random = new Random();

    public PowerUp(Type type, int posX, int posY, GameEngine game) {
        this.game = game;
        this.type = type;
        this.posX = posX;
        this.posY = posY;
        this.isActive = true;
    }

    public void activate() {
        System.out.println("PowerUp activated "+ type.name());
        switch (type) {
            case SCORE_BOOSTER:
                increaseScoreMultipler();
                break;
            case FREEZE:
                freezeGhosts();
                break;
            case EXTRA_LIFE:
                grantExtraLife();
                break;
            case INVULNERABILITY:
                makePacmanInvulnerable();
                break;
            case SLOW_GHOSTS:
                slowDownGhosts();
                break;
        }
        isActive = false;
    }

    private void increaseScoreMultipler(){
        game.setScoreMultipler(game.getScoreMultipler()*3);
        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            game.setScoreMultipler(1);
        }).start();
    }


    private void freezeGhosts() {
        for (int i = 0; i < game.getGhostSpeed().length; i++) {
            game.getGhostSpeed()[i] = 0;
        }
        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            game.resetGhostSpeed();
        }).start();
    }

    private void grantExtraLife() {
        game.incresePacmanLifes();
    }

    private void makePacmanInvulnerable() {
        game.setInvulnerable(true);
        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            game.setInvulnerable(false);
        }).start();
    }

    private void slowDownGhosts() {
        for (int i = 0; i < game.getGhostSpeed().length; i++) {
            game.setGhostSpeed(i, Math.max(1, game.getGhostSpeed()[i] / 2));
        }
        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            game.resetGhostSpeed();
        }).start();
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}