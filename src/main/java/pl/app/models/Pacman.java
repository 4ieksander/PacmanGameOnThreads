package pl.app.models;

public class Pacman {
    private int x, y; // Pozycje Pacmana na planszy
    private Board board;

    public Pacman(Board board) {
        this.board = board;
        this.x = 1; // startowa pozycja x
        this.y = 1; // startowa pozycja y
    }

    public void move(int dx, int dy) {
        if (board.getTile(x + dx, y + dy) != 1) { // Jeżeli nie ma ściany, to ruch jest możliwy
            x += dx;
            y += dy;
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
