package ac.tiletower.algorithm;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by asilkaratas on 12/4/16.
 */
public class ArrayShiftTest {

    @Test
    public void testShift() {
        final int[] values = {1, 2, 3, 4, 5, 6};
        final int shift = 1;
        final int[] expectedValues = {2, 3, 4, 5, 6, 0};

        System.arraycopy(values, shift, values, 0, values.length-shift);
        Arrays.fill(values, values.length-shift, values.length, 0);

        System.out.println(Arrays.toString(values));
        assertArrayEquals(expectedValues, values);
    }
}
