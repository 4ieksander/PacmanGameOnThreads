package pl.app.models;

public class Board {
    private final int rows = 20; // liczba wierszy
    private final int cols = 20; // liczba kolumn
    private int[][] map = new int[rows][cols];

    public Board() {
        // 0 - puste miejsce, 1 - ściana, 2 - punkt
        // Załaduj tutaj mapę, np. z pliku lub statycznie zdefiniowanej
    }

    public int getTile(int x, int y) {
        return map[x][y];
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int[][] getMap() {
        return map;
    }

    public void setMap(int[][] map) {
        this.map = map;
    }
}