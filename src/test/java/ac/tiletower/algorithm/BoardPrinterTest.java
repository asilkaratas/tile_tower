package ac.tiletower.algorithm;

import ac.tiletower.algorithm.Board;
import ac.tiletower.algorithm.BoardPrinter;
import ac.tiletower.algorithm.Placement;
import ac.tiletower.algorithm.Tile;
import org.junit.Test;

/**
 * Created by asilkaratas on 12/3/16.
 */
public class BoardPrinterTest {

    @Test
    public void testPrint() {
        // ARRANGE
        // arranges original bitmap data
        final int[] data1 = {
                0, 1, 1,
                1, 1, 0,
        };
        final Tile tile1 = new Tile(0, 3, 2, data1);

        // ACT
        final Placement placement1 = new Placement(tile1, tile1.getOriginalBitmap(), 0, 0d);
        final Placement placement2= new Placement(tile1, tile1.getOriginalBitmap(), 3, 0d);
        final Placement placement3= new Placement(tile1, tile1.getBitmaps()[1], 3, 0d);
        final Board board = new Board("Board0", 20, 2, new Tile[1], new int[2], 3);
        board.place(placement1);
        board.place(placement2);
        board.place(placement3);

        final BoardPrinter boardPrinter = new BoardPrinter();
        boardPrinter.print(board);
    }
}
