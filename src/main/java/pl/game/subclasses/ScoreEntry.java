package pl.game.subclasses;
import java.io.Serializable;

public class ScoreEntry implements Serializable {
    private static final long serialVersionUID = 1L; // This is a unique identifier for the class

    private String playerName;
    private int score;

    public ScoreEntry(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }
}
