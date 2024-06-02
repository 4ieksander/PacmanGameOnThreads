package pl.game.subclasses;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreManager {
    private String filePath = "src/main/resources/scores.dat";

    public void saveScores(List<ScoreEntry> scores) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(scores);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<ScoreEntry> loadScores() {
        List<ScoreEntry> scores = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            scores = (List<ScoreEntry>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return scores;
    }
}
