package pl.game.boards;

import pl.game.subclasses.AutomaticBoard;
public class HugeBoard extends AutomaticBoard {
    private final String name;

    public HugeBoard() {
        name = "Huge Board";
        initVariables(40);
        drawBoard();
        addLargeSquare(10, 10, 6);
        addLargeSquare(25, 25, 8);
        addLargeSquare(15, 30, 5);
        addLargeSquare(33, 32, 4);
        addLargeSquare(30, 4, 9);
        addLargeSquare(10, 25, 10);
        addLargeSquare(22,2, 5);
    }


}