package pl.app.game.subclasses;

import pl.app.game.GameEngine;
import java.util.Random;

public class PowerUp {
    public enum Type {
        SUPER_SPEED, FREEZE, EXTRA_LIFE, INVULNERABILITY, SLOW_GHOSTS
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
        switch (type) {
            case SUPER_SPEED:
                boostSpeedTemporarily();
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

    private void boostSpeedTemporarily() {
        game.setCurrentSpeed(game.getCurrentSpeed()+3);
        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            game.setCurrentSpeed(game.getCurrentSpeed()-3);
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
            resetGhostSpeed();
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
                Thread.sleep(7000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            resetGhostSpeed();
        }).start();
    }

    private void resetGhostSpeed() {
        for (int i = 0; i < game.getGhostSpeed().length; i++) {
            game.setGhostSpeed(i, game.getValidSpeeds()[random.nextInt(game.getValidSpeeds().length)] );
        }
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