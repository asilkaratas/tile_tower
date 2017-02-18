package ac.tiletower.algorithm;

import ac.tiletower.algorithm.Bitmap;
import ac.tiletower.algorithm.Board;

import java.util.Arrays;

/**
 * PlacementCalculator does heavy calculations to make for scoring a tile.
 * It is shadow of the board object. 
 */
public final class PlacementCalculator {

    private final int[] holes;
    private final int[] heights;
    private final int[] heightSums;
    private final int[] rowFills;
    private final Board board;

    private int totalHoles;
    private int rowFillsSum;
    private int height;
    private int minHeight;
    private int filled;
    private int y;
    private int rowFillsY;

    public PlacementCalculator(final Board board) {
        this.board = board;
        final int boardWidth = board.getWidth();
        this.holes = new int[boardWidth];
        this.heightSums = new int[boardWidth];
        this.heights = new int[boardWidth];
        this.totalHoles = 0;
        this.rowFillsSum = 0;
        this.rowFillsY = 0;
        this.minHeight = 0;
        this.rowFills = new int[board.getRowFills().length];

        System.arraycopy(board.getHeights(), 0, heights, 0, heights.length);
    }

    public int[] getHeights() {
        return heights;
    }

    public int getHeight() {
        return height;
    }

    public Board getBoard() {
        return board;
    }

    public int[] getHoles() {
        return holes;
    }

    public int getTotalHoles() {
        return totalHoles;
    }

    public int[] getRowFills() {
        return rowFills;
    }

    public int getRowFillsY() {
        return rowFillsY;
    }

    /**
     * Calculates holes.
     * @param bitmap 
     * @param x
     */
    public void calculateHoles(final Bitmap bitmap, final int x) {
        int heightSum;
        int index;
        final int[] boardHeights = board.getHeights();
        final int bitmapWidth = bitmap.getWidth();
        final int[] bitmapHoles = bitmap.getHoles();
        int maxHeightSum = 0;
        for(int i = bitmapWidth-1; i >= 0; i--) {
            index = x + i;
            heightSum = boardHeights[index] - bitmapHoles[i];
            heightSums[index] = heightSum;
            if(heightSum > maxHeightSum) {
                maxHeightSum = heightSum;
            }
        }

        totalHoles = 0;
        int holeCount;
        for(int i = bitmapWidth-1; i >= 0; i--) {
            index = x + i;
            holeCount = maxHeightSum - heightSums[index];
            holes[index] = holeCount;
            totalHoles += holeCount;
        }
    }

    public boolean startCalculation(final Bitmap bitmap, final int x) {
        calculateHoles(bitmap, x);
        calculateHeights(bitmap, x);
        return calculateRowFills(bitmap, x);
    }

    private void calculateHeights(final Bitmap bitmap, final int x) {
        final int[] boardHeights = board.getHeights();
        final int[] bitmapHeights = bitmap.getHeights();
        final int[] bitmapHoles = bitmap.getHoles();
        final int bitmapWidth = bitmap.getWidth();

        int columnHeight;
        int index;
        minHeight = board.getMinHeight();
        height = board.getHeight();
        for(int i = bitmapWidth-1; i >= 0; i--) {
            index = x + i;
            columnHeight = boardHeights[index] + holes[index]
                    - bitmapHoles[i] + bitmapHeights[i];
            heights[index] = columnHeight;
            if(columnHeight > height) {
                height = columnHeight;
            }

            if(columnHeight < minHeight) {
                minHeight = columnHeight;
            }
        }

        filled = board.getFilled() + bitmap.getFilled();
        y = heights[x] - bitmapHeights[0];
    }

    private boolean calculateRowFills(final Bitmap bitmap, final int x) {
        final int[] bitmapRowFills = bitmap.getRowFills();
        final int startIndex = -board.getRowFillsY() + y;
        final int endIndex = startIndex + bitmapRowFills.length;
        if(startIndex < 0 || endIndex > rowFills.length-1) {
            return false;
        }
        //System.out.println("fillsStart2:" + fillsStart + " y:" + y + " x:" + x);
        for(int i = 0; i < bitmapRowFills.length; i++) {
            rowFills[startIndex + i] += bitmapRowFills[i];
        }

        rowFillsSum = board.getRowFillsSum() + bitmap.getFilled();

        int minHeight = Integer.MAX_VALUE;
        for(int i = 0; i < heights.length; i++) {
            if(minHeight > heights[i]) {
                minHeight = heights[i];
            }
        }

        rowFillsY = board.getRowFillsY();
        if(height - rowFillsY > rowFills.length/2) {
            int shift = height-rowFillsY-rowFills.length/2;
            if(rowFillsY + shift>minHeight) {
                shift = minHeight - rowFillsY;
            }
            rowFillsY += shift;
        }

        return true;
        //hasRowFillsYError();
    }

    public boolean hasRowFillsYError() {
        for(int h : heights) {
            if(h < rowFillsY-2) {
                //System.out.println("rowFillsY:" + rowFillsY + " heights:" + Arrays.toString(heights));
                return true;
            }
        }

        return false;
    }

    public void resetHeights(final Bitmap bitmap, final int x) {
        System.arraycopy(board.getHeights(), x, heights, x, bitmap.getWidth());
        height = board.getHeight();

        // reset rowFills
        final int fillsStart = -board.getRowFillsY() + y;
        System.arraycopy(board.getRowFills(), fillsStart, rowFills, fillsStart, bitmap.getHeight());

        //System.out.println("resetHeights");
       // System.out.println(Arrays.toString(board.getRowFills()));
        //System.out.println(Arrays.toString(rowFills));
    }
}
