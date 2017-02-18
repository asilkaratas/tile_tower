package ac.tiletower.algorithm;

import ac.tiletower.algorithm.Bitmap;
import ac.tiletower.algorithm.Tile;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by asilkaratas on 12/2/16.
 */
public class TileTest {

    /**
     * Tests public constructor of Tile class with 4 possible rotations.
     */
    @Test
    public void testConstructorWithFourRotation() {
        // ARRANGE
        // arranges asserting bitmaps]
        final int[] data1 =  {
                1, 0, 0,
                1, 1, 0,
                0, 1, 0,
                1, 1, 1
        };
        final Bitmap bitmap1 = new Bitmap(3, 4, data1);

        final int[] data2 =  {
                1, 0, 1, 1,
                1, 1, 1, 0,
                1, 0, 0, 0
        };
        final Bitmap bitmap2 = new Bitmap(4, 3, data2);

        final int[] data3 =  {
                1, 1, 1,
                0, 1, 0,
                0, 1, 1,
                0, 0, 1
        };
        final Bitmap bitmap3 = new Bitmap(3, 4, data3);

        final int[] data4 =  {
                0, 0, 0, 1,
                0, 1, 1, 1,
                1, 1, 0, 1
        };
        final Bitmap bitmap4 = new Bitmap(4, 3, data4);
        final Bitmap[] bitmaps = {bitmap1, bitmap2, bitmap3, bitmap4};

        // arranges original bitmap data
        final int[] data = {
                1, 0, 0,
                1, 1, 0,
                0, 1, 0,
                1, 1, 1
        };

        // ACT
        final Tile tile = new Tile(0, 3, 4, data);

        // ASSERT
        // asserts that tile has 4 bitmaps
        assertEquals(4, tile.getBitmaps().length);

        // asserts that originalBitmap is bitmap1
        assertEquals(bitmap1, tile.getOriginalBitmap());

        // asserts that bitmaps are rotated in expected order
        for(int i = tile.getBitmaps().length - 1; i >= 0; i--) {
            assertEquals(bitmaps[i], tile.getBitmaps()[i]);
        }
    }

    /**
     * Tests public constructor of Tile class with 2 possible rotations.
     */
    @Test
    public void testConstructorWithTwoRotations() {
        // ARRANGE
        // arranges asserting bitmaps]
        final int[] data1 =  {
                1, 1, 1,
                0, 1, 0,
                0, 1, 0,
                1, 1, 1
        };
        final Bitmap bitmap1 = new Bitmap(3, 4, data1);

        final int[] data2 =  {
                1, 0, 0, 1,
                1, 1, 1, 1,
                1, 0, 0, 1
        };
        final Bitmap bitmap2 = new Bitmap(4, 3, data2);
        final Bitmap[] bitmaps = {bitmap1, bitmap2};

        // arranges original bitmap data
        final int[] data = {
                1, 1, 1,
                0, 1, 0,
                0, 1, 0,
                1, 1, 1
        };

        // ACT
        final Tile tile = new Tile(0, 3, 4, data);

        // ASSERT
        // asserts that tile has 2 bitmaps
        assertEquals(2, tile.getBitmaps().length);

        // asserts that originalBitmap is bitmap1
        assertEquals(bitmap1, tile.getOriginalBitmap());

        // asserts that bitmaps are rotated in expected order
        for(int i = tile.getBitmaps().length - 1; i >= 0; i--) {
            assertEquals(bitmaps[i], tile.getBitmaps()[i]);
        }
    }

    /**
     * Tests public constructor of Tile class with 1 possible rotations.
     */
    @Test
    public void testConstructorWithOneRotations() {
        // ARRANGE
        // arranges asserting bitmaps]
        final int[] data1 =  {
                1, 1, 1,
                1, 1, 1,
                1, 1, 1
        };
        final Bitmap bitmap1 = new Bitmap(3, 3, data1);
        final Bitmap[] bitmaps = {bitmap1};

        // arranges original bitmap data
        final int[] data = {
                1, 1, 1,
                1, 1, 1,
                1, 1, 1
        };

        // ACT
        final Tile tile = new Tile(0, 3, 3, data);

        // ASSERT
        // asserts that tile has 2 bitmaps
        assertEquals(1, tile.getBitmaps().length);

        // asserts that originalBitmap is bitmap1
        assertEquals(bitmap1, tile.getOriginalBitmap());

        // asserts that bitmaps are rotated in expected order
        for(int i = tile.getBitmaps().length - 1; i >= 0; i--) {
            assertEquals(bitmaps[i], tile.getBitmaps()[i]);
        }
    }
}
