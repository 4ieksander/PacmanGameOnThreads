package pl.game.subclasses;
import java.io.Serializable;

public class ScoreEntry implements Serializable {
    private static final long serialVersionUID = 1L; // This is a unique identifier for the class

    private String playerName;
    private int score;
    private String boardSize;
    private int livesLeft;
    private long timeElapsed;

    public ScoreEntry(String playerName, int score, String boardSize, int livesLeft, long timeElapsed) {
        this.playerName = playerName;
        this.score = score;
        this.boardSize  = boardSize;
        this.livesLeft = livesLeft;
        this.timeElapsed = timeElapsed;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }

    public long getTimeElapsed() {
        return timeElapsed;
    }

    public int getLivesLeft() {
        return livesLeft;
    }

    public String getBoardSize() {
        return boardSize;
    }
}
