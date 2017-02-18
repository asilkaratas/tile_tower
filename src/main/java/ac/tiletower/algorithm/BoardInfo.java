package ac.tiletower.algorithm;

/**
 * BoardInfo stores board information. 
 * It is purpose to share board information between threads,
 */
public class BoardInfo implements Comparable<BoardInfo> {
    private final Placement lastPlacement;
    private final int rowFillsY;
    private final int rowFillsHeight;
    private final int width;
    private final int height;
    private final double density;

    public BoardInfo(final Board board) {
        this.lastPlacement = board.getLastPlacement();
        this.rowFillsY = board.getRowFillsY();
        this.rowFillsHeight = board.getRowFills().length;
        this.width = board.getWidth();
        this.height = board.getHeight();
        this.density = board.getDensity();
    }

    /**
     * 
     * @return Returns last placement.
     */
    public Placement getLastPlacement() {
        return lastPlacement;
    }

    /**
     *
     * @return Returns row fills y.
     */
    public int getRowFillsY() {
        return rowFillsY;
    }

    public int getRowFillsHeight() {
        return rowFillsHeight;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getDensity() {
        return density;
    }

    @Override
    public int compareTo(BoardInfo o) {
        if(density < o.density) {
            return 1;
        } else if(density > o.density) {
            return -1;
        } else {
            return 0;
        }
    }
}
