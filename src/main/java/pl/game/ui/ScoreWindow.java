package pl.game.ui;
import javax.swing.*;
import java.awt.*;
import pl.game.subclasses.ScoreManager;
import pl.game.subclasses.ScoreEntry;
import pl.game.subclasses.Style;

import java.util.List;
import java.util.Random;


public class ScoreWindow extends JFrame {
    private ScoreManager scoreManager = new ScoreManager();
    private DefaultListModel<String> scoreModel = new DefaultListModel<>();
    private JList<String> scoreList = new JList<>(scoreModel);

    public ScoreWindow() {
        setSize(300, 400);
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
        scores.forEach(score -> scoreModel.addElement(score.getPlayerName() + " - " + score.getScore()));
    }
}
