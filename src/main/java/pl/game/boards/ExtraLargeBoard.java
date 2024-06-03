package pl.game.boards;

import pl.game.subclasses.AutomaticBoard;
public class ExtraLargeBoard extends AutomaticBoard {
    private final String name;

    public ExtraLargeBoard() {
        name = "Extra Large Board";
        initVariables(30);
        drawBoard();
        addLargeSquare(10, 10, 6);
        addLargeSquare(16, 3, 8);
        addLargeSquare(20, 11, 8);
        addLargeSquare(15, 20, 5);
        addLargeSquare(3, 21, 7);

    }
}