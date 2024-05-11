package pl.game.interfaces;


// This map is the representation of 5 binary bits in the decimal system
// binary: 0, 0, 0, 0, 0 => point present, (walls) boottom, right, up, left
// ex. 26 (decimal) -> 1 1 0 1 0 (binary) -> point exists, bottom and up
public interface IBoard {
    short[] getLevelData();
    int getN_BLOCKS();
    int getSCREEN_SIZE();
    String getName();
}
