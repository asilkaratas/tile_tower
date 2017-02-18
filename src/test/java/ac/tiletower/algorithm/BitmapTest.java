package ac.tiletower.algorithm;

import ac.tiletower.algorithm.Bitmap;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by asilkaratas on 11/30/16.
 */
public class BitmapTest {


    @Test
    public void testConstructor() {
        // ARRANGE
        // arranges bitmap data
        final int[] data = {
                1, 1, 0, 0,
                1, 0, 1, 0,
                1, 1, 1, 1,
                1, 1, 0, 0
        };

        // ACT
        final Bitmap bitmap = new Bitmap(4, 4, data);

        // ASSERT
        // arranges assert data
        final int[] expectedHeights = {4, 4, 3, 2};
        final int[] expectedHoles = {0, 0, 1, 1};
        final int expectedMiddleHoles = 1;
        final int[] expectedRowFills = {2, 4, 2, 2};

        assertEquals(10, bitmap.getFilled());
        assertEquals(4, bitmap.getWidth());
        assertEquals(4, bitmap.getHeight());
        assertEquals(expectedMiddleHoles, bitmap.getMiddleHoles());
        assertArrayEquals(expectedHeights, bitmap.getHeights());
        assertArrayEquals(expectedHoles, bitmap.getHoles());
        assertArrayEquals(expectedRowFills, bitmap.getRowFills());
    }

    @Test
    public void testRotate() {
        // ARRANGE
        // arranges bitmap data
        final int[] data = {
                1, 0, 0, 0,
                1, 0, 1, 0,
                1, 1, 1, 1
        };
        final Bitmap bitmap = new Bitmap(4, 3, data);

        // ACT
        final Bitmap rotatedBitmap = bitmap.rotate();

        // ASSERT
        // arranges rotated bitmap
        final int[] expectedRotatedData = {
                1, 1, 1,
                1, 0, 0,
                1, 1, 0,
                1, 0, 0
        };
        final Bitmap expectedRotatedBitmap = new Bitmap(3, 4, expectedRotatedData);
        assertEquals(expectedRotatedBitmap, rotatedBitmap);
    }

    @Test
    public void testRotate2() {
        // ARRANGE
        // arranges bitmap data
        final int[] data = {
                1, 1, 1,
                1, 0, 0,
                1, 1, 0,
                1, 0, 0
        };
        final Bitmap bitmap = new Bitmap(3, 4, data);

        // ACT
        final Bitmap rotatedBitmap = bitmap.rotate();

        // ASSERT
        // arranges rotated bitmap
        final int[] expectedRotatedData = {
                1, 1, 1, 1,
                0, 1, 0, 1,
                0, 0, 0, 1
        };
        final Bitmap expectedRotatedBitmap = new Bitmap(4, 3, expectedRotatedData);
        assertEquals(expectedRotatedBitmap, rotatedBitmap);
    }

    @Test
    public void testRotate3() {
        // ARRANGE
        // arranges bitmap data
        final int[] data = {
                1, 1, 1, 1,
                0, 1, 0, 1,
                0, 0, 0, 1
        };
        final Bitmap bitmap = new Bitmap(4, 3, data);

        // ACT
        final Bitmap rotatedBitmap = bitmap.rotate();

        // ASSERT
        // arranges rotated bitmap
        final int[] expectedRotatedData = {
                0, 0, 1,
                0, 1, 1,
                0, 0, 1,
                1, 1, 1
        };
        final Bitmap expectedRotatedBitmap = new Bitmap(3, 4, expectedRotatedData);
        assertEquals(expectedRotatedBitmap, rotatedBitmap);
    }

    @Test
    public void testRotate4() {
        // ARRANGE
        // arranges bitmap data
        final int[] data = {
                0, 0, 1,
                0, 1, 1,
                0, 0, 1,
                1, 1, 1
        };
        final Bitmap bitmap = new Bitmap(3, 4, data);

        // ACT
        final Bitmap rotatedBitmap = bitmap.rotate();

        // ASSERT
        // arranges rotated bitmap
        final int[] expectedRotatedData = {
                1, 0, 0, 0,
                1, 0, 1, 0,
                1, 1, 1, 1
        };
        final Bitmap expectedRotatedBitmap = new Bitmap(4, 3, expectedRotatedData);
        assertEquals(expectedRotatedBitmap, rotatedBitmap);
    }
}
