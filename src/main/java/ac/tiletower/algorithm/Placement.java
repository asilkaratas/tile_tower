package ac.tiletower.algorithm;

import java.io.Serializable;

/**
 * Placement stores scored placement.
 */
public class Placement implements Serializable{
    private final int x;
    private final Tile tile;
    private final Bitmap bitmap;
    private final double score;


    private Placement parent;
    private int y;
    private int rowFillsY;
    private Board board;
    private int index;

    public Placement(final Tile tile, final Bitmap bitmap, final int x, final double score) {
        this.tile = tile;
        this.bitmap = bitmap;
        this.x = x;
        this.score = score;
        this.parent = null;
        this.y = 0;
        this.board = null;
        this.index = 0;
        this.rowFillsY = 0;
    }

    /**
     * 
     * @return a tile
     */
    public Tile getTile() {
        return tile;
    }

    /**
     * 
     * @return a bitmap that placed
     */
    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public double getScore() {
        return score;
    }

    public void setParent(Placement parent) {
        this.parent = parent;
    }

    public Placement getParent() {
        return parent;
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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setRowFillsY(int rowFillsY) {
        this.rowFillsY = rowFillsY;
    }

    public int getRowFillsY() {
        return rowFillsY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Placement placement = (Placement) o;

        if (x != placement.x) return false;
        if (Double.compare(placement.score, score) != 0) return false;
        return bitmap.equals(placement.bitmap);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = x;
        result = 31 * result + tile.hashCode();
        result = 31 * result + bitmap.hashCode();
        temp = Double.doubleToLongBits(score);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return String.format("Placement x:%s y:%s score:%.3f rowFillsY:%d",
                x, y, score, rowFillsY);
    }
}
