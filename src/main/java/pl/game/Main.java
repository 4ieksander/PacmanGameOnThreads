package pl.game;

import pl.game.ui.MenuFrame;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            MenuFrame ex = new MenuFrame();
            ex.setVisible(true);
        });}}

