package pl.game.subclasses;
import javax.swing.*;
import java.awt.*;

public class Style {
    public static final Color BACKGROUND_COLOR = new Color(25, 25, 112);
    public static final Color TEXT_COLOR = Color.WHITE;
    public static final Font TEXT_FONT = new Font("Arial", Font.BOLD, 14);

    public static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(25, 150, 70));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(200, 50));
        return button;
    }


    public static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT_COLOR);
        label.setFont(TEXT_FONT);
        return label;
    }
}
