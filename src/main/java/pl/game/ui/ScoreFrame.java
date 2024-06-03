package pl.game.ui;
import javax.swing.*;
import java.awt.*;
import pl.game.subclasses.ScoreManager;
import pl.game.subclasses.ScoreEntry;
import pl.game.subclasses.Style;

import java.util.List;


public class ScoreFrame extends JFrame {
    private ScoreManager scoreManager = new ScoreManager();
    private DefaultListModel<String> scoreModel = new DefaultListModel<>();
    private JList<String> scoreList = new JList<>(scoreModel);

    public ScoreFrame() {
        setSize(600, 400);
        setLayout(new BorderLayout());

        JButton showScoresButton = Style.createButton("Show Scores");
        showScoresButton.addActionListener(e -> displayScores());
        add(showScoresButton, BorderLayout.SOUTH);

        scoreList.setBackground(Style.BACKGROUND_COLOR);
        scoreList.setForeground(Style.TEXT_COLOR);
        scoreList.setFont(Style.TEXT_FONT);

        JScrollPane scrollPane = new JScrollPane(scoreList);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        getContentPane().setBackground(Style.BACKGROUND_COLOR);
        setLocationRelativeTo(null);

    }

    private void displayScores() {
        List<ScoreEntry> scores = scoreManager.loadScores();
        scoreModel.clear();
        for (int i = 0; i < scores.size(); i++) {
            scoreModel.addElement(formatScore(scores.get(i), i + 1));
        }    }

    private String formatScore(ScoreEntry score, int rank) {
        return String.format(
                "%d. %s -  %d, %s, %d seconds, %d lives left.",
                rank, score.getPlayerName(), score.getScore(), score.getBoardSize(), score.getTimeElapsed(), score.getLivesLeft()
        );
    }

}
