package pl.app.models;

public class Pacman {
    private int x, y; // Pozycje Pacmana na planszy
    private Board board;

    public Pacman() {
        this.x = 1; // startowa pozycja x
        this.y = 1; // startowa pozycja y
    }

    public void move(int dx, int dy, Board board) {
        int newX = x + dx;
        int newY = y + dy;
        // Sprawdź, czy na nowej pozycji nie ma ściany
        if (!board.isWall(newX, newY)) {
            x = newX;
            y = newY;
        }
    }
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}
