package ac.tiletower.io;

import ac.tiletower.algorithm.Tile;

import java.io.Serializable;
import java.util.List;

/**
 * Created by asilkaratas on 12/3/16.
 */
public class InputData implements Serializable {

    private int boardWidth;
    private Tile[] tiles;

    public InputData(final int boardWidth, final Tile[] tiles) {
        this.boardWidth = boardWidth;
        this.tiles = tiles;
    }

    public Tile[] getTiles() {
        return tiles;
    }

    public int getBoardWidth() {
        return boardWidth;
    }
}
